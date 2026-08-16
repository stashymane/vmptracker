package dev.stashy.vmptracker.screens.settings.sections

import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.stashy.vmptracker.icons.Icons
import dev.stashy.vmptracker.icons.outlined.Face24Dp
import dev.stashy.vmptracker.icons.outlined.Speed24Dp
import dev.stashy.vmptracker.icons.outlined.Visibility24Dp
import dev.stashy.vmptracker.model.settings.Runner
import dev.stashy.vmptracker.screens.settings.SettingsViewmodel
import dev.stashy.vmptracker.ui.components.InlineIcon
import dev.stashy.vmptracker.ui.components.RadioButtonRow
import dev.stashy.vmptracker.ui.components.settings.SettingEntry
import dev.stashy.vmptracker.ui.components.settings.SettingsSection
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vmptracker.app.Res
import vmptracker.app.settings_face_enabled_subtitle
import vmptracker.app.settings_face_enabled_title
import vmptracker.app.settings_face_section
import vmptracker.app.settings_model_runner_cpu
import vmptracker.app.settings_model_runner_gpu
import vmptracker.app.settings_model_runner_subtitle
import vmptracker.app.settings_model_runner_title

@Composable
fun FaceTrackerSettingsSection(
    vm: SettingsViewmodel
) {
    val scope = rememberCoroutineScope()
    val settings by vm.faceTrackerSettings.collectAsStateWithLifecycle()

    SettingsSection({
        InlineIcon(Icons.Outlined.Face24Dp)
        Text(stringResource(Res.string.settings_face_section))
    }) {
        val updateEnabled: (Boolean) -> Unit = { enabled ->
            scope.launch { vm.updateFaceTracker { it.copy(enabled = enabled) } }
        }
        SettingEntry(
            { Text(stringResource(Res.string.settings_face_enabled_title)) },
            icon = { Icon(Icons.Outlined.Visibility24Dp, null) },
            subtitle = { Text(stringResource(Res.string.settings_face_enabled_subtitle)) },
            onClick = { updateEnabled(!settings.enabled) },
            label = { Switch(settings.enabled, updateEnabled, interactionSource = it) }
        )

        SettingEntry(
            { Text(stringResource(Res.string.settings_model_runner_title)) },
            icon = { Icon(Icons.Outlined.Speed24Dp, null) },
            subtitle = { Text(stringResource(Res.string.settings_model_runner_subtitle)) },
            label = { Text(stringResource(settings.runner.toResource())) }
        ) {
            RadioButtonRow(
                Runner.entries,
                settings.runner,
                { runner -> scope.launch { vm.updateFaceTracker { it.copy(runner = runner) } } }) {
                Text(stringResource(it.toResource()))
            }
        }
    }
}

fun Runner.toResource() = when (this) {
    CPU -> Res.string.settings_model_runner_cpu
    GPU -> Res.string.settings_model_runner_gpu
}

@ComponentPreview
@Composable
private fun FaceTrackerSettingsSectionPreview() = PreviewHost {
    val vm = remember { SettingsViewmodel() }
    FaceTrackerSettingsSection(vm)
}
