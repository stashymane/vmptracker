package dev.stashy.vmptracker.ui.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import org.jetbrains.compose.resources.stringResource
import vmptracker.app.Res
import vmptracker.app.settings_camera_lens_subtitle
import vmptracker.app.settings_camera_lens_title

@Composable
fun SettingsSection(
    header: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) = Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
    header?.let { header ->
        SettingsSectionHeader(Modifier.padding(horizontal = 16.dp)) {
            header()
        }
    }

    SettingsSectionContent {
        content()
    }
}

@ComponentPreview
@Composable
private fun SettingsSectionPreview() = PreviewHost {
    SettingsSection(
        header = {
            Text("Section")
        }
    ) {
        SettingEntry(
            { Text(stringResource(Res.string.settings_camera_lens_title)) },
            subtitle = { Text(stringResource(Res.string.settings_camera_lens_subtitle)) }
        ) {
            TextButton({}) {
                Text("Back 1x")
            }
        }
    }
}
