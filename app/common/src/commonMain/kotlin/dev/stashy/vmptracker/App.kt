package dev.stashy.vmptracker

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.stashy.vmptracker.model.settings.ColorMode
import dev.stashy.vmptracker.ui.LocalSettings
import dev.stashy.vmptracker.ui.Navigation
import dev.stashy.vmptracker.ui.theme.AppTheme

@Composable
expect fun App()

@Composable
fun AppScreen() {
    val isDark = when (LocalSettings.current.mode) {
        ColorMode.Auto -> isSystemInDarkTheme()
        ColorMode.Light -> false
        ColorMode.Dark -> true
    }

    AppTheme(Color.Red, isDark) {
        Surface(Modifier.fillMaxSize()) {
            Navigation()
        }
    }
}
