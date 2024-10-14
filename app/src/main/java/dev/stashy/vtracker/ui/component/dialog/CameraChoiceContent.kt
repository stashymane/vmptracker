package dev.stashy.vtracker.ui.component.dialog

import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalLensFacing
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.stashy.vtracker.ui.theme.VTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraChoiceContent(
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    val context = LocalContext.current
    val providerFuture = remember { ProcessCameraProvider.getInstance(context) }
    val availableCameras = remember { mutableStateListOf<CameraInfo>() }

    LaunchedEffect(Unit) {
        val provider = providerFuture.get()
        availableCameras.addAll(provider.availableCameraInfos)
    }

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
        Text("${it.cameraId()} (${stringResource(it.facing())})")
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


@androidx.annotation.OptIn(ExperimentalCamera2Interop::class)
fun CameraInfo.cameraId() = Camera2CameraInfo.from(this).cameraId

@Preview
@Composable
private fun CameraChoiceDialogPreview() {
    VTrackerTheme {
        CameraChoiceContent({}, {})
    }
}