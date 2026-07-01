package dev.stashy.vmptracker.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Path
import dev.stashy.vmptracker.model.SettingsActions
import dev.stashy.vmptracker.model.settings.AppSettings

val LocalBackStack: ProvidableCompositionLocal<AppBackStack> =
    compositionLocalOf { error("LocalBackStack not initialized.") }

val LocalSettings: ProvidableCompositionLocal<AppSettings> =
    compositionLocalOf { AppSettings() }

val LocalSettingsActions: ProvidableCompositionLocal<SettingsActions> =
    staticCompositionLocalOf { error("Settings actions not provided") }

val LocalSnackbarState: ProvidableCompositionLocal<SnackbarHostState> =
    staticCompositionLocalOf { SnackbarHostState() }

val LocalDeviceCorners: ProvidableCompositionLocal<Path?> =
    staticCompositionLocalOf { null }
