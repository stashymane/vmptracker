package dev.stashy.vmptracker.model.settings

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val mode: ColorMode = ColorMode.Auto,
    val displayPreview: Boolean = true
)

@Serializable
enum class ColorMode {
    Auto,
    Light,
    Dark
}
