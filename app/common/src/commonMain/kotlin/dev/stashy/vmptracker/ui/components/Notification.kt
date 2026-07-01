package dev.stashy.vmptracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.icons.Icons
import dev.stashy.vmptracker.icons.outlined.Face24Dp
import dev.stashy.vmptracker.icons.outlined.FrontHand24Dp
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import vmptracker.app.Res
import vmptracker.app.error_tracking_failed
import vmptracker.app.warning_battery_low_content
import vmptracker.app.warning_battery_low_title

@Composable
fun Notification(
    title: StringResource,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceContainer,
    icon: ImageVector? = null,
    content: (@Composable () -> Unit)? = null
) = Surface(
    modifier,
    shape = MaterialTheme.shapes.medium,
    color = color
) {
    Box(Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let { icon ->
                Icon(icon, null)
            }

            Column {
                Text(
                    stringResource(title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                content?.let { content ->
                    ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                        Row {
                            content()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BatteryLevelNotification() {
    Notification(
        Res.string.warning_battery_low_title,
        color = MaterialTheme.colorScheme.errorContainer,
        icon = Icons.Outlined.FrontHand24Dp
    ) {
        Text(
            stringResource(
                Res.string.warning_battery_low_content
            )
        )
    }
}

@ComponentPreview
@Composable
private fun NotificationPreview() = PreviewHost {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Notification(
            Res.string.error_tracking_failed
        )

        Notification(
            Res.string.error_tracking_failed
        ) {
            Text("Detailed error description with suggestion")
        }

        Notification(
            Res.string.error_tracking_failed,
            icon = Icons.Outlined.Face24Dp
        ) {
            Text("Detailed error description with suggestion")
        }

        Notification(
            Res.string.error_tracking_failed,
            icon = Icons.Outlined.Face24Dp
        )

        BatteryLevelNotification()
    }
}
