package dev.stashy.vmptracker.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.stashy.vmptracker.camera.CameraController
import dev.stashy.vmptracker.camera.NoOpCameraController
import dev.stashy.vmptracker.icons.Icons
import dev.stashy.vmptracker.icons.outlined.Camera24Dp
import dev.stashy.vmptracker.icons.outlined.Face24Dp
import dev.stashy.vmptracker.icons.outlined.PhotoCamera24Dp
import dev.stashy.vmptracker.icons.outlined.Visibility24Dp
import dev.stashy.vmptracker.model.Screens
import dev.stashy.vmptracker.screens.settings.components.CameraFrameRatePicker
import dev.stashy.vmptracker.ui.LocalBackStack
import dev.stashy.vmptracker.ui.components.InlineIcon
import dev.stashy.vmptracker.ui.components.selectedCameraLensLabel
import dev.stashy.vmptracker.ui.components.settings.SettingEntry
import dev.stashy.vmptracker.ui.components.settings.SettingsSection
import dev.stashy.vmptracker.ui.theme.DevicePreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import vmptracker.app.Res
import vmptracker.app.screen_title_settings
import vmptracker.app.settings_camera_lens_subtitle
import vmptracker.app.settings_camera_lens_title
import vmptracker.app.settings_camera_section
import vmptracker.app.settings_capture_framerate_subtitle
import vmptracker.app.settings_capture_framerate_title
import vmptracker.app.settings_capture_framerate_value
import vmptracker.app.settings_face_section

@Composable
fun SettingsScreen(
    vm: SettingsViewmodel = koinViewModel(),
    cameraController: CameraController = koinInject(),
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val settings by vm.settings.collectAsStateWithLifecycle()
    val captureState by cameraController.captureState.collectAsStateWithLifecycle()
    val lensState by cameraController.lensState.collectAsStateWithLifecycle()
    val backStack = LocalBackStack.current
    val selectedLensLabel = selectedCameraLensLabel(lensState)
    val captureFrameRateOptions = captureState.supportedFrameRates
        .ifEmpty { listOf(settings.captureFrameRate) }

    Scaffold { paddingValues ->
        Column(
            Modifier.verticalScroll(scrollState).padding(paddingValues).padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp).padding(top = 16.dp)) {
                Text(
                    stringResource(Res.string.screen_title_settings),
                    style = MaterialTheme.typography.displaySmallEmphasized
                )
            }

            SettingsSection({
                InlineIcon(Icons.Outlined.PhotoCamera24Dp)
                Text(stringResource(Res.string.settings_camera_section))
            }) {
                SettingEntry(
                    title = { Text(stringResource(Res.string.settings_camera_lens_title)) },
                    subtitle = { Text(stringResource(Res.string.settings_camera_lens_subtitle)) },
                    icon = { Icon(Icons.Outlined.Camera24Dp, null) },
                    onClick = if (lensState.lenses.size > 1) {
                        { backStack.add(Screens.CameraLensPicker) }
                    } else {
                        null
                    },
                ) {
                    Text(
                        selectedLensLabel,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                SettingEntry(
                    title = { Text(stringResource(Res.string.settings_capture_framerate_title)) },
                    icon = { Icon(Icons.Outlined.Camera24Dp, null) },
                    subtitle = { Text(stringResource(Res.string.settings_capture_framerate_subtitle)) },
                    compact = false
                ) {
                    CameraFrameRatePicker(captureFrameRateOptions, settings.captureFrameRate, {
                        scope.launch { vm.setCaptureFrameRate(it) }
                    })
                }
            }

            SettingsSection({
                InlineIcon(Icons.Outlined.Face24Dp)
                Text(stringResource(Res.string.settings_face_section))
            }) {
                SettingEntry(
                    { Text(stringResource(Res.string.settings_face_section)) },
                    icon = { Icon(Icons.Outlined.Visibility24Dp, null) },
                    subtitle = { Text(stringResource(Res.string.settings_face_section)) },
                    onClick = {}
                ) {
                    Switch(false, {})
                }
            }
        }
    }
}

@DevicePreview
@Composable
private fun SettingsScreenPreview() = PreviewHost {
    SettingsScreen(
        vm = viewModel(),
        cameraController = NoOpCameraController,
    )
}
