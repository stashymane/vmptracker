package dev.stashy.vtracker.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.stashy.vtracker.R
import dev.stashy.vtracker.ui.theme.VTrackerTheme

@Composable
fun CameraControlRow(
    isActive: Boolean = false,
    onPickLens: () -> Unit,
    onStart: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val startButtonColor by animateColorAsState(
        if (isActive)
            MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceContainer, label = "run button color"
    )
    val startButtonContentColor by animateColorAsState(
        if (isActive)
            MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurface, label = "run button content color"
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        IconButton(onPickLens) {
            Icon(Icons.Default.Cameraswitch, "Lens change")
        }

        Button(
            onStart,
            colors = ButtonDefaults.buttonColors(
                containerColor = startButtonColor,
                contentColor = startButtonContentColor
            )
        ) {
            AnimatedContent(isActive, label = "start button text") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (it) {
                        Icon(imageVector = Icons.Default.Stop, null)
                        Text(stringResource(R.string.stop_button))
                    } else {
                        Icon(imageVector = Icons.Default.PlayArrow, null)
                        Text(stringResource(R.string.start_button))
                    }
                }
            }
        }

        IconButton(onSettings) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
    }
}

@Preview
@Composable
private fun CameraControlRowPreview() {
    VTrackerTheme {
        CameraControlRow(false, {}, {}, {})
    }
}