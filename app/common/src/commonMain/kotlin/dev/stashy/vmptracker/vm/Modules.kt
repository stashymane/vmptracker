package dev.stashy.vmptracker.vm

import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun viewmodelModule(): Module = module {
    viewModelOf(::CameraViewmodel)
    viewModelOf(::SettingsViewmodel)
}
