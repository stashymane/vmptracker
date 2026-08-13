package dev.stashy.vmptracker.model.settings

import kotlinx.serialization.Serializable

@Serializable
data class CameraSettings(
    val displayPreview: Boolean = true,
    val captureFrameRate: Int = 60,
)
