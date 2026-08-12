package dev.stashy.vmptracker.ui.camera

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.stashy.vmptracker.camera.CameraControllerImpl
import dev.stashy.vmptracker.ui.LocalSettings
import org.koin.compose.koinInject

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun CameraPreviewEffect() {
    val controller = koinInject<CameraControllerImpl>()
    val settings = LocalSettings.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(cameraPermission.status) {
        if (!cameraPermission.status.isGranted) {
            cameraPermission.launchPermissionRequest()
        }
    }

    LaunchedEffect(
        cameraPermission.status.isGranted,
        settings.displayPreview,
        settings.captureFrameRate,
    ) {
        if (!cameraPermission.status.isGranted) return@LaunchedEffect

        controller.setPreferredFrameRate(settings.captureFrameRate)
        if (settings.displayPreview) {
            controller.startPreview(lifecycleOwner)
        } else {
            controller.stopPreview()
        }
    }

    DisposableEffect(lifecycleOwner) {
        onDispose { controller.stopPreview() }
    }
}
