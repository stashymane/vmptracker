package dev.stashy.vmptracker.model

import dev.stashy.vmptracker.model.settings.AppSettings
import dev.stashy.vmptracker.model.settings.CameraSettings
import dev.stashy.vmptracker.model.settings.FaceTrackerSettings

interface SettingsActions {
    suspend fun updateGeneral(transform: (AppSettings) -> AppSettings)

    suspend fun updateCamera(transform: (CameraSettings) -> CameraSettings)

    suspend fun updateFaceTracker(transform: (FaceTrackerSettings) -> FaceTrackerSettings)
}
