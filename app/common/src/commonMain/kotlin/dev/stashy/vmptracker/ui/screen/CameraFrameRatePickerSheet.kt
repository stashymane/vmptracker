package dev.stashy.vmptracker.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.stashy.vmptracker.camera.CameraController
import dev.stashy.vmptracker.camera.NoOpCameraController
import dev.stashy.vmptracker.ui.LocalBackStack
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import dev.stashy.vmptracker.vm.SettingsViewmodel
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
        Modifier.fillMaxWidth().padding(bottom = 64.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            stringResource(Res.string.settings_capture_framerate_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        )

        LazyColumn {
            items(frameRateOptions, key = { it }) { fps ->
                val selected = fps == settings.captureFrameRate

                ListItem(
                    headlineContent = {
                        Text(
                            stringResource(Res.string.settings_capture_framerate_value, fps),
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        )
                    },
                    leadingContent = {
                        RadioButton(
                            selected = selected,
                            onClick = null,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch {
                                vm.setCaptureFrameRate(fps)
                                backStack.removeLast()
                            }
                        },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                )
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
