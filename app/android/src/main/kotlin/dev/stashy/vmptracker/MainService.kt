package dev.stashy.vmptracker

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import dev.stashy.vmptracker.camera.CameraControllerImpl
import org.koin.android.ext.android.inject

class MainService : LifecycleService() {
    private val cameraController: CameraControllerImpl by inject()

    inner class LocalBinder : Binder() {
        val service: MainService = this@MainService
    }

    private val binder = LocalBinder()

    override fun onCreate() {
        super.onCreate()
        setupNotificationChannel()
        cameraController.attachService(
            onStarted = { startCameraForeground() },
            onStopped = {
                ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
            },
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onDestroy() {
        cameraController.detachService()
        super.onDestroy()
    }
}
