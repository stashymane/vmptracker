package dev.stashy.vtracker.ui.component.dialog

import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalLensFacing
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CameraFront
import androidx.compose.material.icons.filled.CameraRear
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.stashy.vtracker.service.CameraService
import dev.stashy.vtracker.ui.theme.VTrackerTheme
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraChoiceContent(
    service: CameraService,
    current: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    val availableCameras = service.getAvailableCameras()

    ListDialogContent(
        availableCameras,
        { Text(stringResource(dev.stashy.vtracker.R.string.setting_camera_title)) },
        onDismiss,
        {
            onSelect(it.cameraId())
            onDismiss()
        },
        Modifier.fillMaxWidth()
    ) {
        val id = it.cameraId()
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(it.facingIcon(), null)
            Column {
                Text("${stringResource(dev.stashy.vtracker.R.string.setting_camera_title)} $id")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(it.facing()))
                    Text("${round(it.intrinsicZoomRatio * 10) / 10}x")
                }
            }
            Spacer(Modifier.weight(1f))
            if (id == current) Text(stringResource(dev.stashy.vtracker.R.string.setting_camera_current))
        }
    }
}

@androidx.annotation.OptIn(ExperimentalLensFacing::class)
fun CameraInfo.facing() =
    when (lensFacing) {
        CameraSelector.LENS_FACING_FRONT -> dev.stashy.vtracker.R.string.camera_facing_front
        CameraSelector.LENS_FACING_BACK -> dev.stashy.vtracker.R.string.camera_facing_rear
        CameraSelector.LENS_FACING_EXTERNAL -> dev.stashy.vtracker.R.string.camera_facing_external
        else -> dev.stashy.vtracker.R.string.camera_facing_unknown
    }

@androidx.annotation.OptIn(ExperimentalLensFacing::class)
fun CameraInfo.facingIcon() =
    when (lensFacing) {
        CameraSelector.LENS_FACING_FRONT -> Icons.Default.CameraFront
        CameraSelector.LENS_FACING_BACK -> Icons.Default.CameraRear
        CameraSelector.LENS_FACING_EXTERNAL -> Icons.Default.CameraAlt
        else -> Icons.Default.QuestionMark
    }


@androidx.annotation.OptIn(ExperimentalCamera2Interop::class)
fun CameraInfo.cameraId() = Camera2CameraInfo.from(this).cameraId

@Preview
@Composable
private fun CameraChoiceDialogPreview() {
    VTrackerTheme {
        CameraChoiceContent(TODO(), "", {}, {})
    }
}