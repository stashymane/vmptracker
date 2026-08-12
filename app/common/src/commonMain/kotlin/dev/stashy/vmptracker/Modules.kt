package dev.stashy.vmptracker

import dev.stashy.vmptracker.screens.camera.CameraViewmodel
import dev.stashy.vmptracker.screens.settings.SettingsViewmodel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun viewmodelModule(): Module = module {
    viewModelOf(::CameraViewmodel)
    viewModelOf(::SettingsViewmodel)
}
