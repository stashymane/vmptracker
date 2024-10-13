package dev.stashy.vtracker.service

import androidx.camera.core.ImageProxy
import dev.stashy.vtracker.service.tracking.FaceTracker
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.StateFlow

interface TrackerService {
    sealed class Status {
        object NotRunning : Status()
        object Running : Status()
        class Error(val exception: Exception) : Status()
    }

    val status: StateFlow<Status>
    val frames: ReceiveChannel<ImageProxy>
    val results: ReceiveChannel<Result<FaceTracker.Frame>>

    fun start()
    fun stop()
}