package dev.stashy.vmptracker.camera

import androidx.camera.core.SurfaceRequest
import kotlinx.coroutines.flow.StateFlow

/** Android preview surface exposed to the viewfinder composable. */
interface CameraPreviewHost {
    val surfaceRequest: StateFlow<SurfaceRequest?>
}
