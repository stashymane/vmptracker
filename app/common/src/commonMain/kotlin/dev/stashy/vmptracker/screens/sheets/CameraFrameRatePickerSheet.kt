package dev.stashy.vmptracker.screens.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.stashy.vmptracker.camera.CameraController
import dev.stashy.vmptracker.camera.NoOpCameraController
import dev.stashy.vmptracker.screens.settings.SettingsViewmodel
import dev.stashy.vmptracker.screens.settings.components.CameraFrameRatePicker
import dev.stashy.vmptracker.ui.components.settings.SettingsSection
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import vmptracker.app.Res
import vmptracker.app.settings_capture_framerate_title

@Composable
fun CameraFrameRatePickerSheet(
    vm: SettingsViewmodel = koinViewModel(),
    cameraController: CameraController = koinInject(),
) {
    val scope = rememberCoroutineScope()
    val settings by vm.cameraSettings.collectAsStateWithLifecycle()
    val captureState by cameraController.captureState.collectAsStateWithLifecycle()
    val frameRateOptions by remember {
        derivedStateOf { captureState.supportedFrameRates.ifEmpty { listOf(settings.captureFrameRate) } }
    }

    Column(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SettingsSection({
            Text(stringResource(Res.string.settings_capture_framerate_title))
        }) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                CameraFrameRatePicker(frameRateOptions, settings.captureFrameRate, {
                    scope.launch {
                        vm.update(settings.copy(captureFrameRate = it.coerceIn(1, 240)))
                    }
                })
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
