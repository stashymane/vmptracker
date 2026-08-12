package dev.stashy.vmptracker.camera

import androidx.camera.lifecycle.ProcessCameraProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun cameraModule() = module {
    single {
        ProcessCameraProvider.getInstance(androidContext()).get()
    }
    singleOf(::CameraControllerImpl) {
        bind<CameraController>()
        bind<CameraPreviewHost>()
    }
}
