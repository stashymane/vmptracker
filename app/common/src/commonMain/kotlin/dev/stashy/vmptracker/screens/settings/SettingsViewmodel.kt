package dev.stashy.vmptracker.screens.settings

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stashy.vmptracker.model.SettingsActions
import dev.stashy.vmptracker.model.settings.AppSettings
import dev.stashy.vmptracker.model.settings.CameraSettings
import dev.stashy.vmptracker.model.settings.InMemoryDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SettingsViewmodel(
    private val appSettingsStore: DataStore<AppSettings> = InMemoryDataStore(AppSettings()),
    private val cameraSettingsStore: DataStore<CameraSettings> = InMemoryDataStore(CameraSettings()),
) : ViewModel(), SettingsActions {
    val settings: StateFlow<AppSettings> = appSettingsStore.data.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppSettings(),
    )
    val cameraSettings: StateFlow<CameraSettings> = cameraSettingsStore.data.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        CameraSettings(),
    )

    override suspend fun update(settings: AppSettings) {
        appSettingsStore.updateData { settings }
    }

    override suspend fun update(settings: CameraSettings) {
        cameraSettingsStore.updateData { settings }
    }
}
