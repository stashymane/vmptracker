package dev.stashy.vtracker.ui

import android.Manifest
import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import dev.stashy.vtracker.ui.screen.CameraPermissionScreen
import dev.stashy.vtracker.ui.theme.VTrackerTheme
import dev.stashy.vtracker.ui.vm.MainViewmodel
import org.koin.compose.koinInject

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Main(mainViewmodel: MainViewmodel = koinInject()) {
    val trackerService by mainViewmodel.tracker.collectAsState()
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    VTrackerTheme {
        Surface {
            AnimatedContent(
                cameraPermission.status,
                label = "permission screen"
            ) { permissionStatus ->
                when (permissionStatus) {
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