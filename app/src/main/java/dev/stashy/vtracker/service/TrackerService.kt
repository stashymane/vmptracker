package dev.stashy.vtracker.service

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
    val results: ReceiveChannel<Result<FaceTracker.Frame>>

    fun startTracking()
    fun stopTracking()
}