package dev.stashy.vtracker.service

import android.content.Context
import android.hardware.camera2.CaptureRequest
import android.util.Range
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.Camera
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import dev.stashy.vtracker.service.tracking.Tracker
import kotlinx.coroutines.flow.MutableStateFlow

interface CameraService {
    val cameraProvider: ProcessCameraProvider
    val previewUseCase: Preview
    val surfaceRequests: MutableStateFlow<SurfaceRequest?>

    fun getAvailableCameras(): List<CameraInfo> =
        cameraProvider.availableCameraInfos

    fun startAnalyzer(
        context: Context,
        cameraId: String,
        trackers: List<Tracker<*, *>>
    ): Camera {
        val selector = cameraSelectorFromId(cameraId)
        if (!cameraProvider.hasCamera(selector)) throw NullPointerException("Camera with ID $cameraId was not found.")
        val info = cameraProvider.getCameraInfo(cameraSelectorFromId(cameraId))

        val imageAnalysis = ImageAnalysis.Builder()
            .setResolutionSelector(
                ResolutionSelector.Builder()
                    .setResolutionStrategy(ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY).build()
            )
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
            .apply {
                setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    TrackerAnalyzer(trackers, info.lensFacing == CameraSelector.LENS_FACING_FRONT)
                )
            }

        return cameraProvider.bindToLifecycle(context as LifecycleOwner, selector, imageAnalysis)
    }

    fun startPreview(
        lifecycleOwner: LifecycleOwner,
        cameraId: String,
        rotation: Int,
    ): Camera {
        val selector = cameraSelectorFromId(cameraId)
        if (!cameraProvider.hasCamera(selector)) throw NullPointerException("Camera with ID $cameraId was not found.")

        previewUseCase.setSurfaceProvider { newSurfaceRequest ->
            surfaceRequests.value = newSurfaceRequest
        }

        return cameraProvider.bindToLifecycle(lifecycleOwner, selector, previewUseCase)
    }

    fun stopPreview() {
        previewUseCase.surfaceProvider = null
        cameraProvider.unbind(previewUseCase)
    }

    @OptIn(ExperimentalCamera2Interop::class)
    fun previewUseCase(): Preview =
        Preview.Builder().apply {
            Camera2Interop.Extender(this)
                .setCaptureRequestOption(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, Range(30, 60))
        }.build()
}

@OptIn(ExperimentalCamera2Interop::class)
fun cameraSelectorFromId(id: String) = CameraSelector.Builder()
    .addCameraFilter { it.filter { Camera2CameraInfo.from(it).cameraId == id } }.build()
