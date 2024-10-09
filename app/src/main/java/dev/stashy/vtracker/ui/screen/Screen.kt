package dev.stashy.vtracker.ui.screen

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data object Settings : Screen()
}