package dev.stashy.vtracker.ui

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dev.stashy.vtracker.ui.screen.PermissionRequestScreen
import dev.stashy.vtracker.ui.theme.VTrackerTheme
import dev.stashy.vtracker.ui.vm.MainViewmodel
import org.koin.compose.koinInject

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Main(mainViewmodel: MainViewmodel = koinInject()) {
    val trackerService by mainViewmodel.tracker.collectAsState()
    val permissions = rememberMultiplePermissionsState(
        buildList {
            add(Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                add(Manifest.permission.POST_NOTIFICATIONS)
        }
    )

    VTrackerTheme {
        Surface {
            AnimatedContent(
                permissions.allPermissionsGranted,
                label = "permission screen"
            ) { permissionStatus ->
                when (permissionStatus) {
                    true -> Layout()
                    else -> PermissionRequestScreen(request = { permissions.launchMultiplePermissionRequest() })
                }
            }
        }
    }
}