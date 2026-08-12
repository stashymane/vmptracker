package dev.stashy.vmptracker.camera

import android.util.Range
import androidx.camera.core.CameraInfo
import androidx.camera.core.Preview

internal object CameraSessionFactory {
    const val VIEWFINDER_MAX_FPS = 60

    data class PreviewSetup(
        val preview: Preview,
        val captureState: CameraCaptureState,
    )

    fun createPreview(
        cameraInfo: CameraInfo,
        onSurfaceRequest: Preview.SurfaceProvider,
        preferredFps: Int,
    ): PreviewSetup {
        val viewfinderFps = preferredFps.coerceAtMost(VIEWFINDER_MAX_FPS)
        val frameRateRange = selectTargetFrameRate(cameraInfo, preferredFps = viewfinderFps)
        val preview = Preview.Builder()
            .setTargetFrameRate(frameRateRange)
            .build()
            .also { useCase -> useCase.surfaceProvider = onSurfaceRequest }

        return PreviewSetup(
            preview = preview,
            captureState = CameraCaptureState(
                frameRate = frameRateRange.upper.coerceAtMost(VIEWFINDER_MAX_FPS),
                supportedFrameRates = supportedFrameRates(cameraInfo),
            ),
        )
    }

    fun supportedFrameRates(cameraInfo: CameraInfo): List<Int> {
        val displayRates = listOf(30, 60, 90, 120, 240)
        return displayRates.filter { fps ->
            cameraInfo.supportedFrameRateRanges.any { range -> fps in range.lower..range.upper }
        }
    }

    private fun selectTargetFrameRate(cameraInfo: CameraInfo, preferredFps: Int): Range<Int> {
        val ranges = cameraInfo.supportedFrameRateRanges
        val exact = ranges.filter { range ->
            preferredFps in range.lower..range.upper
        }
        if (exact.isNotEmpty()) {
            return exact.minBy { range -> range.upper - range.lower }
                .let { Range(preferredFps, preferredFps) }
        }

        return ranges
            .filter { range -> range.upper >= preferredFps }
            .minByOrNull { range -> range.upper }
            ?: ranges.maxByOrNull { range -> range.upper }
            ?: Range(preferredFps, preferredFps)
    }
}
