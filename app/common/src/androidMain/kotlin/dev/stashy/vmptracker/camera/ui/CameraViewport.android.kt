package dev.stashy.vmptracker.camera.ui

import android.Manifest
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.viewfinder.core.ImplementationMode
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.stashy.vmptracker.camera.CameraPreviewHost
import dev.stashy.vmptracker.ui.LocalCameraSettings
import org.koin.compose.koinInject

/**
 * Displays the live CameraX preview. Permission requests and session binding are handled by
 * [CameraPreviewEffect]; this composable only reflects [LocalCameraSettings.displayPreview].
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun CameraViewport(modifier: Modifier) {
    val previewHost = koinInject<CameraPreviewHost>()
    val settings = LocalCameraSettings.current
    val motionScheme = MaterialTheme.motionScheme
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    val surfaceRequest by previewHost.surfaceRequest.collectAsStateWithLifecycle()
    val phase = ViewportPhase.from(
        displayPreview = settings.displayPreview,
        permissionGranted = cameraPermission.status.isGranted,
        surfaceRequest = surfaceRequest,
        performanceMode = settings.previewPerformance
    )

    AnimatedContent(
        targetState = phase,
        modifier = modifier,
        transitionSpec = {
            fadeIn(motionScheme.slowSpatialSpec()) togetherWith
                    fadeOut(motionScheme.slowSpatialSpec()) using null
        },
        contentKey = { target ->
            when (target) {
                ViewportPhase.Hidden -> "hidden"
                is ViewportPhase.Ready -> target.surfaceRequest
            }
        },
        label = "camera viewport",
    ) { target ->
        when (target) {
            ViewportPhase.Hidden -> Unit

            is ViewportPhase.Ready -> {
                Box(Modifier.fillMaxSize().background(Color.Black)) {
                    CameraXViewfinder(
                        surfaceRequest = target.surfaceRequest,
                        implementationMode = if (target.performanceMode) ImplementationMode.EXTERNAL else ImplementationMode.EMBEDDED,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
