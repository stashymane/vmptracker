package dev.stashy.vmptracker.camera

data class CameraZoomState(
    val zoomRatio: Float = 1f,
    val minZoomRatio: Float = 1f,
    val maxZoomRatio: Float = 1f,
    val stops: List<Float> = listOf(1f),
)
