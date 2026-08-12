package dev.stashy.vmptracker.screens.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.stashy.vmptracker.camera.CameraController
import dev.stashy.vmptracker.camera.NoOpCameraController
import dev.stashy.vmptracker.icons.Icons
import dev.stashy.vmptracker.icons.outlined.Check24Dp
import dev.stashy.vmptracker.screens.settings.SettingsViewmodel
import dev.stashy.vmptracker.ui.LocalBackStack
import dev.stashy.vmptracker.ui.components.settings.SettingEntry
import dev.stashy.vmptracker.ui.components.settings.SettingsSection
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import vmptracker.app.Res
import vmptracker.app.settings_capture_framerate_title
import vmptracker.app.settings_capture_framerate_value

@Composable
fun CameraFrameRatePickerSheet(
    vm: SettingsViewmodel = koinViewModel(),
    cameraController: CameraController = koinInject(),
) {
    val backStack = LocalBackStack.current
    val scope = rememberCoroutineScope()
    val settings by vm.settings.collectAsStateWithLifecycle()
    val captureState by cameraController.captureState.collectAsStateWithLifecycle()
    val frameRateOptions = captureState.supportedFrameRates
        .ifEmpty { listOf(settings.captureFrameRate) }

    Column(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SettingsSection({
            Text(stringResource(Res.string.settings_capture_framerate_title))
        }) {
            frameRateOptions.forEach { fps ->
                val selected = fps == settings.captureFrameRate

                SettingEntry(
                    title = {
                        Text(
                            stringResource(Res.string.settings_capture_framerate_value, fps),
                            fontWeight = if (selected) Bold else Normal,
                        )
                    },
                    onClick = {
                        scope.launch {
                            vm.setCaptureFrameRate(fps)
                            backStack.removeLast()
                        }
                    }) {
                    if (selected)
                        Icon(Icons.Outlined.Check24Dp, null)
                }
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton({
                backStack.removeLast()
            }) {
                Text("Cancel")
            }
        }
    }
}

@ComponentPreview
@Composable
private fun CameraFrameRatePickerSheetPreview() = PreviewHost {
    val vm = remember { SettingsViewmodel() }
    CameraFrameRatePickerSheet(vm, NoOpCameraController)
}
