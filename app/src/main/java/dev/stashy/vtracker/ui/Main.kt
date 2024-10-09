package dev.stashy.vtracker.ui

import android.Manifest
import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import dev.stashy.vtracker.ui.screen.CameraPermissionScreen
import dev.stashy.vtracker.ui.theme.VTrackerTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Main() {
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    VTrackerTheme {
        Surface {
            AnimatedContent(cameraPermission.status, label = "permission screen") {
                when (it) {
                    PermissionStatus.Granted -> {
                        Layout()
                    }

                    else -> {
                        CameraPermissionScreen(request = { cameraPermission.launchPermissionRequest() })
                    }
                }
            }
        }
    }
}