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
import androidx.lifecycle.coroutineScope
import dev.stashy.vtracker.model.settings.GeneralSettings
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class CameraXService(lifecycleOwner: LifecycleOwner) : CameraService, KoinComponent,
    LifecycleOwner by lifecycleOwner {
    override val surfaceRequests: MutableStateFlow<SurfaceRequest?> = MutableStateFlow(null)

    private val cameraProvider: ProcessCameraProvider by inject()
    private val generalSettings: DataStore<GeneralSettings> by inject(named<GeneralSettings>())
    private val cameraIdFlow
        get() = generalSettings.data.map { it.cameraId }.distinctUntilChanged().filterNotNull()

    private val useCases = arrayOf(analysisUseCase, previewUseCase)
    private val cameraSwapJob: Job = lifecycle.coroutineScope.launch {
        cameraIdFlow.collect { cameraId ->
            val activeUseCases =
                useCases.filter { case -> cameraProvider.isBound(case) }.toTypedArray()

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this@CameraXService,
                cameraSelectorFromId(cameraId),
                *activeUseCases
            )
        }
    }

    override fun getAvailableCameras(): List<CameraInfo> =
        cameraProvider.availableCameraInfos.filter { !it.isLogicalMultiCameraSupported }
            .sortedBy { it.lensFacing }

    override fun start(useCase: UseCase) {
        stop(useCase)
        // the runblocking call sucks but cba to do it properly atm
        cameraProvider.bindToLifecycle(
            this,
            cameraSelectorFromId(runBlocking { cameraIdFlow.first() }),
            useCase
        )
    }

    override fun stop(useCase: UseCase) {
        cameraProvider.unbind(useCase)
    }

    override fun bindPreview() {
        previewUseCase.setSurfaceProvider { newSurfaceRequest ->
            surfaceRequests.value = newSurfaceRequest
        }
    }

    override fun unbindPreview() {
        previewUseCase.surfaceProvider = null
    }
}

@androidx.annotation.OptIn(ExperimentalCamera2Interop::class)
fun cameraSelectorFromId(id: String) = CameraSelector.Builder()
    .addCameraFilter { it.filter { Camera2CameraInfo.from(it).cameraId == id } }.build()
