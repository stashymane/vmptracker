package dev.stashy.vtracker.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dev.stashy.vtracker.tracking.FaceTracker

object FaceTrackerService : Service() {
    private val faceTracker = FaceTracker()
    

    override fun onBind(intent: Intent?): IBinder? = null
}