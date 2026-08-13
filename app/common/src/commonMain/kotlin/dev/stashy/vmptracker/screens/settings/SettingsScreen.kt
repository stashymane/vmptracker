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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.stashy.vmptracker.camera.CameraController
import dev.stashy.vmptracker.camera.NoOpCameraController
import dev.stashy.vmptracker.icons.Icons
import dev.stashy.vmptracker.icons.outlined.Face24Dp
import dev.stashy.vmptracker.icons.outlined.Visibility24Dp
import dev.stashy.vmptracker.screens.settings.sections.CameraSettingsSection
import dev.stashy.vmptracker.ui.components.InlineIcon
import dev.stashy.vmptracker.ui.components.settings.SettingEntry
import dev.stashy.vmptracker.ui.components.settings.SettingsSection
import dev.stashy.vmptracker.ui.theme.DevicePreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import vmptracker.app.Res
import vmptracker.app.screen_title_settings
import vmptracker.app.settings_face_section

@Composable
fun SettingsScreen(
    vm: SettingsViewmodel = koinViewModel(),
    cameraController: CameraController = koinInject(),
) {
    val scrollState = rememberScrollState()

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

            CameraSettingsSection(vm, cameraController)

            SettingsSection({
                InlineIcon(Icons.Outlined.Face24Dp)
                Text(stringResource(Res.string.settings_face_section))
            }) {
                SettingEntry(
                    { Text(stringResource(Res.string.settings_face_section)) },
                    icon = { Icon(Icons.Outlined.Visibility24Dp, null) },
                    subtitle = { Text(stringResource(Res.string.settings_face_section)) },
                    onClick = {},
                    label = { Switch(false, {}) }
                )
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
