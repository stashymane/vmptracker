package dev.stashy.vmptracker.model

sealed class TrackingState {
    object NotRunning : TrackingState()
    object Starting : TrackingState()
    object Running : TrackingState()
    class Failed(val reason: String) : TrackingState()
}
