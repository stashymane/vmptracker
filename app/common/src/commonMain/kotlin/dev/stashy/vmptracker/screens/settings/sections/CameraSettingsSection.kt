package dev.stashy.vmptracker.screens.settings.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.stashy.vmptracker.camera.CameraController
import dev.stashy.vmptracker.camera.CameraLensState
import dev.stashy.vmptracker.camera.LensFacing
import dev.stashy.vmptracker.camera.NoOpCameraController
import dev.stashy.vmptracker.icons.Icons
import dev.stashy.vmptracker.icons.outlined.BatteryAndroidFrameShield24Dp
import dev.stashy.vmptracker.icons.outlined.Camera24Dp
import dev.stashy.vmptracker.icons.outlined.PhotoCamera24Dp
import dev.stashy.vmptracker.icons.outlined.Speed24Dp
import dev.stashy.vmptracker.screens.settings.SettingsViewmodel
import dev.stashy.vmptracker.screens.settings.components.CameraFrameRatePicker
import dev.stashy.vmptracker.screens.settings.components.CameraLensPicker
import dev.stashy.vmptracker.ui.components.InlineIcon
import dev.stashy.vmptracker.ui.components.selectedCameraLensLabel
import dev.stashy.vmptracker.ui.components.settings.SettingEntry
import dev.stashy.vmptracker.ui.components.settings.SettingsSection
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vmptracker.app.Res
import vmptracker.app.settings_camera_lens_subtitle
import vmptracker.app.settings_camera_lens_title
import vmptracker.app.settings_camera_section
import vmptracker.app.settings_capture_framerate_subtitle
import vmptracker.app.settings_capture_framerate_title
import vmptracker.app.settings_capture_framerate_value
import vmptracker.app.settings_viewfinder_performance_subtitle
import vmptracker.app.settings_viewfinder_performance_title

@Composable
fun CameraSettingsSection(
    vm: SettingsViewmodel,
    cameraController: CameraController
) {
    val scope = rememberCoroutineScope()

    val settings by vm.cameraSettings.collectAsStateWithLifecycle()
    val captureState by cameraController.captureState.collectAsStateWithLifecycle()
    val lensState by cameraController.lensState.collectAsStateWithLifecycle()

    val selectedLensLabel = selectedCameraLensLabel(lensState)
    val captureFrameRateOptions = captureState.supportedFrameRates
        .ifEmpty { listOf(settings.captureFrameRate) }

    var showDialog by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SettingsSection({
            InlineIcon(Icons.Outlined.PhotoCamera24Dp)
            Text(stringResource(Res.string.settings_camera_section))
        }) {
            SettingEntry(
                title = { Text(stringResource(Res.string.settings_camera_lens_title)) },
                subtitle = { Text(stringResource(Res.string.settings_camera_lens_subtitle)) },
                icon = { Icon(Icons.Outlined.Camera24Dp, null) },
                onClick = { showDialog = true },
                label = { Text(selectedLensLabel) }
            )

            SettingEntry(
                title = { Text(stringResource(Res.string.settings_capture_framerate_title)) },
                icon = { Icon(Icons.Outlined.Speed24Dp, null) },
                subtitle = { Text(stringResource(Res.string.settings_capture_framerate_subtitle)) },
                label = {
                    Text(
                        stringResource(
                            Res.string.settings_capture_framerate_value,
                            settings.captureFrameRate
                        )
                    )
                }
            ) {
                CameraFrameRatePicker(captureFrameRateOptions, settings.captureFrameRate, { fps ->
                    scope.launch {
                        vm.updateCamera { it.copy(captureFrameRate = fps.coerceIn(1, 240)) }
                    }
                })
            }
        }

        SettingsSection {
            val togglePreviewPerformance: (Boolean) -> Unit =
                { enabled -> scope.launch { vm.updateCamera { it.copy(previewPerformance = enabled) } } }
            SettingEntry(
                title = { Text(stringResource(Res.string.settings_viewfinder_performance_title)) },
                icon = { Icon(Icons.Outlined.BatteryAndroidFrameShield24Dp, null) },
                subtitle = { Text(stringResource(Res.string.settings_viewfinder_performance_subtitle)) },
                onClick = { togglePreviewPerformance(!settings.previewPerformance) },
                label = {
                    Switch(settings.previewPerformance, togglePreviewPerformance, interactionSource = it)
                }
            )
        }
    }

    if (showDialog) {
        Dialog({ showDialog = false }) {
            CameraLensDialog(lensState) { lensId ->
                cameraController.selectLens(lensId)
                scope.launch {
                    vm.updateCamera { settings ->
                        val lens = lensState.lenses.find { it.id == lensId }
                        val zoom = lens?.let {
                            if (it.facing == LensFacing.Back) it.intrinsicZoomRatio else 1f
                        }
                        settings.copy(
                            selectedLensId = lensId,
                            zoomRatio = zoom ?: settings.zoomRatio,
                        )
                    }
                }
                showDialog = false
            }
        }
    }
}

@Composable
private fun CameraLensDialog(
    lensState: CameraLensState,
    onSelect: (String) -> Unit
) {
    Surface(
        Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                stringResource(Res.string.settings_camera_lens_title),
                style = MaterialTheme.typography.headlineMedium
            )

            CameraLensPicker(lensState.lenses, lensState.selectedId) {
                onSelect(it)
            }
        }
    }
}

@ComponentPreview
@Composable
private fun CameraSettingsSectionPreview() = PreviewHost {
    val vm = remember { SettingsViewmodel() }

    CameraSettingsSection(vm, NoOpCameraController)
}

@ComponentPreview
@Composable
private fun CameraLensDialogPreview() = PreviewHost {
    val lensState by NoOpCameraController.lensState.collectAsStateWithLifecycle()

    CameraLensDialog(lensState) {}
}


