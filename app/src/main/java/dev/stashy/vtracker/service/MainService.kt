package dev.stashy.vtracker.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.camera.core.ImageProxy
import androidx.core.app.ServiceCompat
import dev.stashy.vtracker.model.settings.FaceTrackerSettings
import dev.stashy.vtracker.service.TrackerService.Status
import dev.stashy.vtracker.service.camera.CameraService
import dev.stashy.vtracker.service.camera.CameraXService
import dev.stashy.vtracker.service.tracking.FaceTracker
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent

class MainService(val cameraXService: CameraService = CameraXService()) : Service(), AppService,
    KoinComponent, CameraService by cameraXService {
    private val faceTracker = FaceTracker()

    override val status: MutableStateFlow<Status> = MutableStateFlow(Status.NotRunning)
    override val frames: ReceiveChannel<ImageProxy>
        get() = TODO("Not yet implemented")

    override val results get() = faceTracker.results

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
        cameraXService.stopAll()
        faceTracker.stop()
    }

    internal fun setupForegroundService() {
        try {
            ServiceCompat.startForeground(
                this,
                100,
                serviceNotification(),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
                else
                    0
            )
        } catch (e: Exception) {
            status.tryEmit(Status.Error(e))
            e.printStackTrace()
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
        fun getService(): AppService = this@MainService
    }

    override fun onBind(intent: Intent?): IBinder? = binder
}