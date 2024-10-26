package dev.stashy.vtracker.service.camera

import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.Camera
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.datastore.core.DataStore
import androidx.lifecycle.LifecycleOwner
import dev.stashy.vtracker.model.settings.GeneralSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class CameraXService(scope: CoroutineScope) : CameraService, KoinComponent,
    CoroutineScope by scope {
    override val surfaceRequests: MutableStateFlow<SurfaceRequest?> = MutableStateFlow(null)

    private val cameraProvider: ProcessCameraProvider by inject()
    private val generalSettings: DataStore<GeneralSettings> by inject(named<GeneralSettings>())

    override fun getAvailableCameras(): List<CameraInfo> =
        cameraProvider.availableCameraInfos

    override fun startAnalyzer(): Camera {
        TODO("Not yet implemented")
        // TODO make this start a coroutine (probably on a seprate thread?) that sends frames for analysis
        // keep track of it for state calculation
    }

    override fun stopAnalyzer() {
        TODO("Not yet implemented")
    }

    override fun startPreview(
        lifecycleOwner: LifecycleOwner,
        cameraId: String,
        rotation: Int
    ): Camera {
        val selector = cameraSelectorFromId(cameraId)
        if (!cameraProvider.hasCamera(selector)) throw NullPointerException("Camera with ID $cameraId was not found.")

        previewUseCase.setSurfaceProvider { newSurfaceRequest ->
            surfaceRequests.value = newSurfaceRequest
        }

        return cameraProvider.bindToLifecycle(lifecycleOwner, selector, previewUseCase)
    }

    override fun stopPreview() {
        previewUseCase.surfaceProvider = null
        cameraProvider.unbind(previewUseCase)
    }

    override fun stopAll() {
        cameraProvider.unbindAll()
    }
}

@androidx.annotation.OptIn(ExperimentalCamera2Interop::class)
fun cameraSelectorFromId(id: String) = CameraSelector.Builder()
    .addCameraFilter { it.filter { Camera2CameraInfo.from(it).cameraId == id } }.build()
