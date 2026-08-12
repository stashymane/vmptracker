package dev.stashy.vmptracker.camera

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface CameraController {
    val zoomState: StateFlow<CameraZoomState>
    val lensState: StateFlow<CameraLensState>
    val captureState: StateFlow<CameraCaptureState>

    fun setZoomRatio(ratio: Float)
    fun selectLens(lensId: String)
    fun setPreferredFrameRate(fps: Int)
    fun startTracking()
    fun stopTracking()
}

object NoOpCameraController : CameraController {
    override val zoomState = MutableStateFlow(CameraZoomState())
    override val lensState = MutableStateFlow(
        CameraLensState(
            lenses = listOf(
                CameraLens("0", LensFacing.Back, 0.5f, 1920, 1080, listOf(30, 60)),
                CameraLens("1", LensFacing.Back, 1f, 3840, 2160, listOf(30, 60, 120)),
                CameraLens("2", LensFacing.Front, 1f, 1920, 1080, listOf(30)),
            ),
            selectedId = "1",
        ),
    )
    override val captureState = MutableStateFlow(
        CameraCaptureState(supportedFrameRates = listOf(30, 60)),
    )

    override fun setZoomRatio(ratio: Float) {}

    override fun selectLens(lensId: String) {}

    override fun setPreferredFrameRate(fps: Int) {}

    override fun startTracking() {}

    override fun stopTracking() {}
}
