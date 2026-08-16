package dev.stashy.vmptracker.screens.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stashy.vmptracker.camera.CameraController
import dev.stashy.vmptracker.camera.CameraLensState
import dev.stashy.vmptracker.camera.CameraZoomState
import dev.stashy.vmptracker.camera.NoOpCameraController
import dev.stashy.vmptracker.model.TrackingState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class CameraViewmodel(
    private val cameraController: CameraController = NoOpCameraController,
) : ViewModel() {
    val trackingState: MutableStateFlow<TrackingState> = MutableStateFlow(Loading)
    val zoomState: StateFlow<CameraZoomState> = cameraController.zoomState
    val lensState: StateFlow<CameraLensState> = cameraController.lensState

    fun setZoomRatio(ratio: Float) = cameraController.setZoomRatio(ratio)

    fun selectLens(lensId: String) = cameraController.selectLens(lensId)

    fun initialize() = viewModelScope.launch {
        delay(1.seconds)
        trackingState.emit(NotRunning)
    }

    fun toggleTracking() {
        when (trackingState.value) {
            Running -> stopTracking()
            is TrackingState.Failed -> viewModelScope.launch {
                trackingState.emit(NotRunning)
            }

            else -> startTracking()
        }
    }

    fun startTracking() = viewModelScope.launch {
        trackingState.emit(Starting)
        delay(1.seconds)
        cameraController.startTracking()
        trackingState.emit(Running)
    }

    fun stopTracking() = viewModelScope.launch {
        cameraController.stopTracking()
        trackingState.emit(NotRunning)
    }
}
