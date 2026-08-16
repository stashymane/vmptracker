package dev.stashy.vmptracker

import androidx.datastore.core.DataStore
import dev.stashy.vmptracker.model.settings.AppSettings
import dev.stashy.vmptracker.model.settings.CameraSettings
import dev.stashy.vmptracker.screens.camera.CameraViewmodel
import dev.stashy.vmptracker.screens.settings.SettingsViewmodel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun viewmodelModule(): Module = module {
    viewModel {
        CameraViewmodel(
            cameraController = get(),
            cameraSettingsStore = get(named<CameraSettings>()),
        )
    }
    viewModel {
        SettingsViewmodel(
            appSettingsStore = get<DataStore<AppSettings>>(named<AppSettings>()),
            cameraSettingsStore = get<DataStore<CameraSettings>>(named<CameraSettings>()),
        )
    }
}
