package dev.stashy.vmptracker.screens.settings

import androidx.lifecycle.ViewModel
import dev.stashy.vmptracker.model.SettingsActions
import dev.stashy.vmptracker.model.settings.AppSettings
import dev.stashy.vmptracker.model.settings.CameraSettings
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewmodel : ViewModel(), SettingsActions {
    val settings: MutableStateFlow<AppSettings> = MutableStateFlow(AppSettings())
    val cameraSettings: MutableStateFlow<CameraSettings> = MutableStateFlow(CameraSettings())

    override suspend fun update(settings: AppSettings) {
        this.settings.emit(settings)
    }

    override suspend fun update(settings: CameraSettings) {
        this.cameraSettings.emit(settings)
    }
}
