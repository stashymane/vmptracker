package dev.stashy.vmptracker.model

import dev.stashy.vmptracker.model.settings.AppSettings
import dev.stashy.vmptracker.model.settings.CameraSettings
import dev.stashy.vmptracker.model.settings.FaceTrackerSettings

interface SettingsActions {
    suspend fun update(settings: AppSettings)

    suspend fun update(settings: CameraSettings)

    suspend fun update(settings: FaceTrackerSettings)
}
