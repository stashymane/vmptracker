package dev.stashy.vmptracker.camera.ui

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.stashy.vmptracker.camera.CameraControllerImpl
import dev.stashy.vmptracker.ui.LocalCameraSettings
import org.koin.compose.koinInject

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun CameraPreviewEffect() {
    val controller = koinInject<CameraControllerImpl>()
    val settings = LocalCameraSettings.current
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
        settings.selectedLensId,
        settings.zoomRatio,
    ) {
        if (!cameraPermission.status.isGranted) return@LaunchedEffect

        settings.selectedLensId?.let(controller::selectLens)
        controller.setPreferredFrameRate(settings.captureFrameRate)
        controller.setPreferredZoomRatio(settings.zoomRatio)
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
