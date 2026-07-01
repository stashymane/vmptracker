package dev.stashy.vmptracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationBar(
    modifier: Modifier = Modifier
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {

    }
}

@Composable
fun Notification(
    title: StringResource,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    subtitle: StringResource? = null,
) {
    Surface(
        modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(stringResource(title))
        }
    }
}

@ComponentPreview
@Composable
private fun NotificationBarPreview() = PreviewHost {
    NotificationBar()
}
