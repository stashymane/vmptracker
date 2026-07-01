package dev.stashy.vmptracker.model

import dev.stashy.vmptracker.model.settings.AppSettings

interface SettingsActions {
    suspend fun update(settings: AppSettings)

    suspend fun toggleViewport()
}
