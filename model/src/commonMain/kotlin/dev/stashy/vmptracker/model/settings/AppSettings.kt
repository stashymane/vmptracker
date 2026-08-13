package dev.stashy.vmptracker.model.settings

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val mode: ColorMode = ColorMode.Auto,
)

@Serializable
enum class ColorMode {
    Auto,
    Light,
    Dark
}
