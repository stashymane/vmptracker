package dev.stashy.vmptracker.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stashy.vmptracker.model.TrackingState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class CameraViewmodel : ViewModel() {
    val trackingState: MutableStateFlow<TrackingState> = MutableStateFlow(TrackingState.NotRunning)

    fun toggleTracking() {
        viewModelScope.launch {
            when (trackingState.value) {
                is Running -> {
                    trackingState.emit(TrackingState.Failed("Unknown"))
                }

                is Failed -> {
                    trackingState.emit(NotRunning)
                }

                else -> {
                    trackingState.emit(Starting)
                    delay(1.seconds)
                    trackingState.emit(Running)
                }
            }
        }
    }

    fun startTracking() {
        viewModelScope.launch {
            trackingState.emit(Starting)
            delay(1.seconds)
            trackingState.emit(Running)
        }
    }

    fun stopTracking() {
        viewModelScope.launch {
            trackingState.emit(NotRunning)
        }
    }
}
