package dev.stashy.vmptracker.screens.camera

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stashy.vmptracker.camera.CameraController
import dev.stashy.vmptracker.camera.CameraLensState
import dev.stashy.vmptracker.camera.CameraZoomState
import dev.stashy.vmptracker.camera.LensFacing
import dev.stashy.vmptracker.camera.NoOpCameraController
import dev.stashy.vmptracker.model.TrackingState
import dev.stashy.vmptracker.model.TrackingState.Loading
import dev.stashy.vmptracker.model.TrackingState.NotRunning
import dev.stashy.vmptracker.model.TrackingState.Running
import dev.stashy.vmptracker.model.TrackingState.Starting
import dev.stashy.vmptracker.model.settings.CameraSettings
import dev.stashy.vmptracker.model.settings.InMemoryDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class CameraViewmodel(
    private val cameraController: CameraController = NoOpCameraController,
    private val cameraSettingsStore: DataStore<CameraSettings> = InMemoryDataStore(CameraSettings()),
) : ViewModel() {
    val trackingState: MutableStateFlow<TrackingState> = MutableStateFlow(Loading)
    val zoomState: StateFlow<CameraZoomState> = cameraController.zoomState
    val lensState: StateFlow<CameraLensState> = cameraController.lensState

    fun setZoomRatio(ratio: Float) {
        cameraController.setPreferredZoomRatio(ratio)
        viewModelScope.launch {
            cameraSettingsStore.updateData { it.copy(zoomRatio = ratio) }
        }
    }

    fun setCaptureFrameRate(fps: Int) {
        val clamped = fps.coerceIn(1, 240)
        cameraController.setPreferredFrameRate(clamped)
        viewModelScope.launch {
            cameraSettingsStore.updateData { it.copy(captureFrameRate = clamped) }
        }
    }

    fun selectLens(lensId: String) {
        cameraController.selectLens(lensId)
        viewModelScope.launch {
            cameraSettingsStore.updateData { settings ->
                val lens = cameraController.lensState.value.lenses.find { it.id == lensId }
                val zoom = lens?.let {
                    if (it.facing == LensFacing.Back) it.intrinsicZoomRatio else 1f
                }
                settings.copy(
                    selectedLensId = lensId,
                    zoomRatio = zoom ?: settings.zoomRatio,
                )
            }
        }
    }

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
