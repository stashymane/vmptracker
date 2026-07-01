package dev.stashy.vmptracker

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleService

class MainService : LifecycleService() {
    inner class LocalBinder : Binder() {
        val service: MainService = this@MainService
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return LocalBinder()
    }
}
