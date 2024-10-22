package dev.stashy.vtracker.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import dev.stashy.vtracker.model.settings.FaceTrackerSettings
import dev.stashy.vtracker.service.TrackerService.Status
import dev.stashy.vtracker.service.tracking.FaceTracker
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow

class TrackerServiceImpl : Service(), AppService {
    override lateinit var cameraProvider: ProcessCameraProvider
    override val previewUseCase: Preview = previewUseCase()
    override val surfaceRequests: MutableStateFlow<SurfaceRequest?> = MutableStateFlow(null)
    private val faceTracker = FaceTracker()

    override val status: MutableStateFlow<Status> = MutableStateFlow(Status.NotRunning)
    override val frames: ReceiveChannel<ImageProxy>
        get() = TODO("Not yet implemented")

    override val results get() = faceTracker.results

    override fun onCreate() {
        super.onCreate()
        cameraProvider = ProcessCameraProvider.getInstance(this).get()
    }

    /**
     * Starts tracking.
     */
    override fun start() {
        setupForegroundService()
        faceTracker.start(this, FaceTrackerSettings())
        status.tryEmit(Status.Running)
    }

    /**
     * Stops tracking.
     */
    override fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        faceTracker.stop()
        status.tryEmit(Status.NotRunning)
    }

    override fun onDestroy() {
        cameraProvider.unbindAll()
        faceTracker.stop()
    }

    internal fun setupForegroundService() {
        try {
            val notification = NotificationCompat.Builder(this, persistentNotificationChannelId)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSilent(true)
                .setContentTitle("VTracker is running")
                .setContentText("Open the app for more details.")
                .build()

            ServiceCompat.startForeground(
                this,
                100,
                notification,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
                else 0
            )
        } catch (e: Exception) {
            status.tryEmit(Status.Error(e))
        }
    }

    /**
     * Starts the service from a received intent, and sets this up as a foreground service.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setupForegroundService()
        return START_STICKY
    }

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): AppService = this@TrackerServiceImpl
    }

    override fun onBind(intent: Intent?): IBinder? = binder
}