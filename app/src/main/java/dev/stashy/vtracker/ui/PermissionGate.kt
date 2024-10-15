package dev.stashy.vtracker.ui

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import dev.chrisbanes.haze.HazeDefaults
import dev.stashy.vtracker.R
import dev.stashy.vtracker.ui.screen.PermissionRequestScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionGate(content: @Composable () -> Unit) {
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    val notificationPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        else null

    CompositionLocalProvider(LocalHazeStyle provides HazeDefaults.style(MaterialTheme.colorScheme.surface)) {
        AnimatedContent(
            cameraPermission to notificationPermission,
            label = "prerequisites"
        ) { perms ->
            if (perms.first.status !is PermissionStatus.Granted)
                PermissionRequestScreen(
                    icon = Icons.Default.Face,
                    title = { Text(stringResource(R.string.camera_required_title)) },
                    description = { Text(stringResource(R.string.camera_required_description)) },
                    request = { perms.first.launchPermissionRequest() }
                )
            else if (perms.second?.status != null && perms.second!!.status !is PermissionStatus.Granted)
                PermissionRequestScreen(
                    icon = Icons.Default.Notifications,
                    title = { Text(stringResource(R.string.notification_required_title)) },
                    description = { Text(stringResource(R.string.notification_required_description)) },
                    request = { perms.second!!.launchPermissionRequest() }
                )
            else content()
        }
    }
}
