package dev.stashy.vtracker.service

import dev.stashy.vtracker.model.tracking.TrackerFrame
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.StateFlow

interface TrackerService {
    sealed class Status {
        object NotRunning : Status()
        object Starting : Status()
        object Running : Status()
        class Error(val exception: Throwable) : Status()
    }

    val status: StateFlow<Status>
    val results: ReceiveChannel<Result<TrackerFrame>>

    fun startTracking()
    fun stopTracking()
}