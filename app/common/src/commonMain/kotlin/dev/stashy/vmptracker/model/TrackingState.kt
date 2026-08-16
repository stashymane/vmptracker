package dev.stashy.vmptracker.model

sealed class TrackingState(val isReady: Boolean) {
    object Loading : TrackingState(false)
    object NotRunning : TrackingState(true)
    object Starting : TrackingState(false)
    object Running : TrackingState(true)
    class Failed(val reason: String) : TrackingState(false)
}
