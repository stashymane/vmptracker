package dev.stashy.vmptracker.screens.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.icons.Icons
import dev.stashy.vmptracker.icons.outlined.Camera24Dp
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import vmptracker.app.Res
import vmptracker.app.settings_camera_lens_subtitle
import vmptracker.app.settings_camera_lens_title

@Composable
fun SettingEntry(
    title: StringResource,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    subtitle: StringResource? = null,
    onClick: (() -> Unit)? = null,
    control: @Composable () -> Unit = {},
) = Surface(
    modifier.fillMaxWidth(),
    shape = MaterialTheme.shapes.small,
    color = MaterialTheme.colorScheme.surfaceContainer
) {
    Column(
        Modifier.let { onClick?.let { onClick -> it.clickable(onClick = onClick) } ?: it }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = ButtonDefaults.MinHeight),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                icon?.let { icon -> Icon(icon, null) }
                Text(stringResource(title), style = MaterialTheme.typography.bodyLarge)
            }

            control()
        }
        subtitle?.let {
            Row(Modifier.padding(bottom = 8.dp).padding(horizontal = 8.dp)) {
                Text(
                    stringResource(subtitle),
                    style = MaterialTheme.typography.bodyMedium.merge(
                        color = LocalContentColor.current.copy(
                            alpha = 0.8f
                        )
                    )
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun SettingEntryPreview() = PreviewHost {
    SettingEntry(
        title = Res.string.settings_camera_lens_title,
        subtitle = Res.string.settings_camera_lens_subtitle,
        icon = Icons.Outlined.Camera24Dp,
    ) {
        Text(
            "Back",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
