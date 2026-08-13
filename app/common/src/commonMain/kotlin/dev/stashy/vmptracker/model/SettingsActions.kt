package dev.stashy.vmptracker.model

import dev.stashy.vmptracker.model.settings.AppSettings
import dev.stashy.vmptracker.model.settings.CameraSettings

interface SettingsActions {
    suspend fun update(settings: AppSettings)

    suspend fun update(settings: CameraSettings)
}
