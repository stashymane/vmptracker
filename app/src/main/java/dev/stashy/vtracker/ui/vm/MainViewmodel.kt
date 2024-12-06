package dev.stashy.vtracker.ui.vm

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stashy.vtracker.model.settings.GeneralSettings
import dev.stashy.vtracker.service.TrackerService
import dev.stashy.vtracker.service.camera.CameraService
import dev.stashy.vtracker.service.camera.previewUseCase
import kotlinx.coroutines.launch

class MainViewmodel(
    val trackerService: TrackerService,
    val cameraService: CameraService,
    val generalSettings: DataStore<GeneralSettings>,
) : ViewModel() {
    val surfaceRequests get() = cameraService.surfaceRequests
    val status get() = trackerService.status

    fun startPreview() = viewModelScope.launch {
        cameraService.start(previewUseCase)
        cameraService.bindPreview()
    }

    fun stopPreview() {
        cameraService.stop(previewUseCase)
        cameraService.unbindPreview()
    }

    fun startTracking() = viewModelScope.launch {
        trackerService.startTracking()
    }

    fun stopTracking() {
        trackerService.stopTracking()
    }

    fun switchCamera(id: String) = viewModelScope.launch {
        generalSettings.updateData { it.copy(cameraId = id) }
    }

    fun showPreview(value: Boolean) = viewModelScope.launch {
        generalSettings.updateData { it.copy(displayPreview = value) }
    }
}