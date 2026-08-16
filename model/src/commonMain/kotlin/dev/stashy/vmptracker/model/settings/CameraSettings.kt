package dev.stashy.vmptracker.model.settings

import kotlinx.serialization.Serializable

@Serializable
data class CameraSettings(
    val displayPreview: Boolean = true,
    val captureFrameRate: Int = 60,
    val previewPerformance: Boolean = false,
    val selectedLensId: String? = null,
    val zoomRatio: Float? = null,
)
