package dev.stashy.vtracker.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import dev.stashy.vtracker.model.FaceTrackerSettings
import dev.stashy.vtracker.service.TrackerService.Status
import dev.stashy.vtracker.service.tracking.FaceTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TrackerServiceImpl : Service(), TrackerService, CoroutineScope by CoroutineScope(Job()) {
    private val faceTracker = FaceTracker()
    private val cameraProvider = ProcessCameraProvider.getInstance(this)

    private val _status: MutableStateFlow<Status> = MutableStateFlow(Status.NotRunning)
    override val status: StateFlow<Status> = _status

    override val frames: ReceiveChannel<ImageProxy>
        get() = TODO("Not yet implemented")

    override val results get() = faceTracker.results

    /**
     * Starts the service from a received intent, and sets this up as a foreground service.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            val notification = NotificationCompat.Builder(this, "CHANNEL_ID")
                .setPriority(NotificationCompat.PRIORITY_LOW)
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
            _status.tryEmit(Status.Error(e))
        }

        return START_STICKY
    }

    /**
     * Starts the tracking.
     */
    override fun start() {
        faceTracker.start(this, FaceTrackerSettings())
        _status.tryEmit(Status.Running)
    }

    override fun stop() {
        faceTracker.stop()
        _status.tryEmit(Status.NotRunning)
    }

    override fun onDestroy() {
        faceTracker.stop()
    }

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService() = this@TrackerServiceImpl
    }

    override fun onBind(intent: Intent?): IBinder? = binder
}