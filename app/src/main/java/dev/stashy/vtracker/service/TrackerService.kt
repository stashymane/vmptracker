package dev.stashy.vtracker.service

import kotlinx.coroutines.flow.StateFlow

interface TrackerService {
    sealed class Status {
        object NotRunning : Status()
        object Starting : Status()
        object Running : Status()
        class Error(val exception: Exception) : Status()
    }

    val status: StateFlow<Status>

    fun start()
    fun stop()
}