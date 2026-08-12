package dev.stashy.vmptracker.camera

enum class LensFacing {
    Front,
    Back,
    External,
    Unknown,
}

data class CameraLens(
    val id: String,
    val facing: LensFacing,
    val intrinsicZoomRatio: Float,
    val maxVideoWidth: Int = 0,
    val maxVideoHeight: Int = 0,
    val supportedFrameRates: List<Int> = emptyList(),
)

data class CameraLensState(
    val lenses: List<CameraLens> = emptyList(),
    val selectedId: String? = null,
)
