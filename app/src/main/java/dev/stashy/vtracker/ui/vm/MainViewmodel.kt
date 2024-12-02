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
) : ViewModel(), TrackerService by trackerService {
    val surfaceRequests get() = cameraService.surfaceRequests

    fun startPreview() {
        cameraService.start(previewUseCase)
        cameraService.startPreview()
    }

    fun stopPreview() {
        cameraService.stopPreview()
        cameraService.stop(previewUseCase)
    }

    fun switchCamera(id: String) = viewModelScope.launch {
        generalSettings.updateData { it.copy(cameraId = id) }
    }

    fun showPreview(value: Boolean) = viewModelScope.launch {
        generalSettings.updateData { it.copy(displayPreview = value) }
    }
}