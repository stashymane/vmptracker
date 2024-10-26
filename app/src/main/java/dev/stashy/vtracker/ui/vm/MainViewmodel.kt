package dev.stashy.vtracker.ui.vm

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stashy.vtracker.model.settings.GeneralSettings
import dev.stashy.vtracker.service.AppService
import kotlinx.coroutines.launch

class MainViewmodel(
    service: AppService,
    val generalSettings: DataStore<GeneralSettings>
) : ViewModel(), AppService by service {
    fun switchCamera(id: String) = viewModelScope.launch {
        generalSettings.updateData { it.copy(cameraId = id) }
    }
    
    fun showPreview(value: Boolean) = viewModelScope.launch {
        generalSettings.updateData { it.copy(displayPreview = value) }
    }
}