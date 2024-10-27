package dev.stashy.vtracker.service.camera

import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.datastore.core.DataStore
import androidx.lifecycle.LifecycleOwner
import dev.stashy.vtracker.model.settings.GeneralSettings
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class CameraXService(lifecycleOwner: LifecycleOwner) : CameraService, KoinComponent,
    LifecycleOwner by lifecycleOwner {
    override val surfaceRequests: MutableStateFlow<SurfaceRequest?> = MutableStateFlow(null)

    private val cameraProvider: ProcessCameraProvider by inject()
    private val generalSettings: DataStore<GeneralSettings> by inject(named<GeneralSettings>())

    override fun getAvailableCameras(): List<CameraInfo> =
        cameraProvider.availableCameraInfos

    override fun start(useCase: UseCase) {
        TODO("Not yet implemented")
    }

    override fun stop(useCase: UseCase) {
        TODO("Not yet implemented")
    }
}

@androidx.annotation.OptIn(ExperimentalCamera2Interop::class)
fun cameraSelectorFromId(id: String) = CameraSelector.Builder()
    .addCameraFilter { it.filter { Camera2CameraInfo.from(it).cameraId == id } }.build()
