package dev.stashy.vmptracker.ui.camera

import androidx.camera.core.SurfaceRequest

internal sealed interface ViewportPhase {
    data object Hidden : ViewportPhase

    data class Ready(
        val surfaceRequest: SurfaceRequest,
    ) : ViewportPhase

    companion object {
        fun from(
            displayPreview: Boolean,
            permissionGranted: Boolean,
            surfaceRequest: SurfaceRequest?,
        ): ViewportPhase = when {
            !displayPreview || !permissionGranted || surfaceRequest == null -> Hidden
            else -> Ready(surfaceRequest)
        }
    }
}
