package dev.stashy.vtracker.service.camera

import androidx.camera.core.CameraInfo
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.UseCase
import kotlinx.coroutines.flow.MutableStateFlow

interface CameraService {
    val surfaceRequests: MutableStateFlow<SurfaceRequest?>

    fun getAvailableCameras(): List<CameraInfo>

    fun start(useCase: UseCase)
    fun stop(useCase: UseCase)
}
