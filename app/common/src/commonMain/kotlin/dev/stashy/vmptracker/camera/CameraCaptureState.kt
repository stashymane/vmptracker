package dev.stashy.vmptracker.camera

data class CameraCaptureState(
    val frameRate: Int = 60,
    val supportedFrameRates: List<Int> = emptyList(),
)
