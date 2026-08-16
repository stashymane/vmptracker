package dev.stashy.vmptracker.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.icons.Icons
import dev.stashy.vmptracker.icons.outlined.Camera24Dp
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import dev.stashy.vmptracker.ui.theme.inDp
import org.jetbrains.compose.resources.stringResource
import vmptracker.app.Res
import vmptracker.app.settings_camera_lens_subtitle
import vmptracker.app.settings_camera_lens_title

@Composable
fun SettingEntry(
    title: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    label: @Composable (MutableInteractionSource) -> Unit = {},
    control: (@Composable (MutableInteractionSource) -> Unit)? = null
) = Surface(
    modifier.fillMaxWidth(),
    shape = MaterialTheme.shapes.small,
    color = containerColor
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        Modifier.let {
            onClick?.let { onClick ->
                it.clickable(onClick = onClick, interactionSource = interactionSource)
            } ?: it
        }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let { icon ->
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryFixed) {
                    val lineHeight = LocalTextStyle.current.lineHeight.inDp()
                    Box(Modifier.size(lineHeight * 1.5f), contentAlignment = Alignment.Center) {
                        icon()
                    }
                }
            }

            Column(
                Modifier.defaultMinSize(minHeight = ButtonDefaults.MinHeight)
                    .width(IntrinsicSize.Min)
                    .weight(1f)
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProvideTextStyle(MaterialTheme.typography.titleMedium) {
                        title()
                    }
                }

                subtitle?.let {
                    ProvideTextStyle(
                        MaterialTheme.typography.bodySmall.merge(
                            color = LocalContentColor.current.copy(
                                alpha = 0.8f
                            )
                        )
                    ) {
                        subtitle()
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProvideTextStyle(MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)) {
                    label(interactionSource)
                }
            }
        }

        control?.let { control ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                control(interactionSource)
            }
        }
    }
}

@Composable
private fun PreviewControl() {
    TextButton({}) {
        Text(
            "Rear Wide (1x)",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@ComponentPreview
@Composable
private fun SettingEntryCompactPreview() = PreviewHost {
    SettingEntry(
        title = { Text(stringResource(Res.string.settings_camera_lens_title)) },
        subtitle = { Text(stringResource(Res.string.settings_camera_lens_subtitle)) },
        icon = { Icon(Icons.Outlined.Camera24Dp, null) },
        label = { PreviewControl() }
    )
}

@ComponentPreview
@Composable
private fun SettingEntryFullPreview() = PreviewHost {
    SettingEntry(
        title = { Text(stringResource(Res.string.settings_camera_lens_title)) },
        subtitle = { Text(stringResource(Res.string.settings_camera_lens_subtitle)) },
        icon = { Icon(Icons.Outlined.Camera24Dp, null) }
    ) { PreviewControl() }
}

@ComponentPreview
@Composable
private fun SettingEntryWithoutSubtitleCompactPreview() = PreviewHost {
    SettingEntry(
        title = { Text(stringResource(Res.string.settings_camera_lens_title)) },
        icon = { Icon(Icons.Outlined.Camera24Dp, null) },
        label = { PreviewControl() }
    )
}

@ComponentPreview
@Composable
private fun SettingEntryWithoutSubtitleFullPreview() = PreviewHost {
    SettingEntry(
        title = { Text(stringResource(Res.string.settings_camera_lens_title)) },
        icon = { Icon(Icons.Outlined.Camera24Dp, null) }
    ) { PreviewControl() }
}
