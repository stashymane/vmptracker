package dev.stashy.vtracker.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.camera.core.ImageProxy
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import dev.stashy.vtracker.model.FaceTrackerSettings
import dev.stashy.vtracker.service.TrackerService.Status
import dev.stashy.vtracker.service.tracking.FaceTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow

class TrackerServiceImpl : Service(), TrackerService, CoroutineScope by CoroutineScope(Job()) {
    private val faceTracker = FaceTracker()

    override val status: MutableStateFlow<Status> = MutableStateFlow(Status.NotRunning)
    override val frames: ReceiveChannel<ImageProxy>
        get() = TODO("Not yet implemented")

    override val results get() = faceTracker.results

    internal fun setupForegroundService() {
        try {
            val notification = NotificationCompat.Builder(this, persistentNotificationChannelId)
                .setPriority(NotificationCompat.PRIORITY_LOW)
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
     * Starts the tracking.
     */
    override fun start() {
        setupForegroundService()
        faceTracker.start(this, FaceTrackerSettings())
        status.tryEmit(Status.Running)
    }

    override fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        faceTracker.stop()
        status.tryEmit(Status.NotRunning)
    }

    override fun onDestroy() {
        faceTracker.stop()
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
        fun getService(): TrackerService = this@TrackerServiceImpl
    }

    override fun onBind(intent: Intent?): IBinder? = binder
}