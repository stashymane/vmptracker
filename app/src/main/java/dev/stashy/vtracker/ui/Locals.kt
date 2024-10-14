package dev.stashy.vtracker.ui

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeStyle

val LocalHazeStyle: ProvidableCompositionLocal<HazeStyle> =
    staticCompositionLocalOf {
        HazeDefaults.style(backgroundColor = Color.Black)
    }
