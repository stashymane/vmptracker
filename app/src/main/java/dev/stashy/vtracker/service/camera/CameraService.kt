package dev.stashy.vtracker.service.camera

import androidx.camera.core.Camera
import androidx.camera.core.CameraInfo
import androidx.camera.core.SurfaceRequest
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow

interface CameraService {
    val surfaceRequests: MutableStateFlow<SurfaceRequest?>

    fun getAvailableCameras(): List<CameraInfo>

    fun startAnalyzer(): Camera
    fun stopAnalyzer()

    fun startPreview(
        lifecycleOwner: LifecycleOwner,
        cameraId: String = "1",
        rotation: Int = 0
    ): Camera

    fun stopPreview()

    fun stopAll()
}
