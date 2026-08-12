package dev.stashy.vmptracker.camera

import android.util.Size
import androidx.camera.core.CameraInfo
import androidx.camera.core.DynamicRange
import androidx.camera.video.Quality
import androidx.camera.video.Recorder

internal data class LensCapabilities(
    val maxVideoWidth: Int = 0,
    val maxVideoHeight: Int = 0,
    val supportedFrameRates: List<Int> = emptyList(),
)

internal object CameraLensCapabilities {
    private val qualityPriority = listOf(
        Quality.UHD,
        Quality.FHD,
        Quality.HD,
        Quality.SD,
        Quality.HIGHEST,
    )

    fun probe(cameraInfo: CameraInfo): LensCapabilities {
        val resolution = maxVideoResolution(cameraInfo)
        return LensCapabilities(
            maxVideoWidth = resolution?.width ?: 0,
            maxVideoHeight = resolution?.height ?: 0,
            supportedFrameRates = CameraSessionFactory.supportedFrameRates(cameraInfo),
        )
    }

    private fun maxVideoResolution(cameraInfo: CameraInfo): Size? {
        val capabilities = runCatching { Recorder.getVideoCapabilities(cameraInfo) }.getOrNull()
            ?: return null
        val dynamicRange = DynamicRange.SDR
        for (quality in qualityPriority) {
            if (!capabilities.isQualitySupported(quality, dynamicRange)) continue
            val size = capabilities.getResolution(quality, dynamicRange) ?: continue
            if (size.width > 0 && size.height > 0) return size
        }
        return null
    }
}
