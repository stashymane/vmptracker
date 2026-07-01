package dev.stashy.vmptracker.vm

import androidx.lifecycle.ViewModel
import dev.stashy.vmptracker.model.SettingsActions
import dev.stashy.vmptracker.model.settings.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewmodel : ViewModel(), SettingsActions {
    val settings: MutableStateFlow<AppSettings> = MutableStateFlow(AppSettings())
    val currentSettings get() = settings.value

    override suspend fun update(settings: AppSettings) {
        this.settings.emit(settings)
    }

    override suspend fun toggleViewport() =
        update(currentSettings.copy(displayPreview = !currentSettings.displayPreview))
}
