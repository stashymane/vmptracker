package dev.stashy.vtracker.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import dev.stashy.vtracker.service.TrackerService
import dev.stashy.vtracker.ui.theme.VTrackerTheme

@Composable
fun CameraControlRow(
    serviceStatus: TrackerService.Status,
    showPreview: Boolean = false,
    onPickLens: () -> Unit,
    onToggleShow: () -> Unit,
    onStart: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val startButtonColor by animateColorAsState(
        when (serviceStatus) {
            is TrackerService.Status.Running -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surfaceContainer
        }, label = "run button color"
    )
    val startButtonContentColor by animateColorAsState(
        when (serviceStatus) {
            is TrackerService.Status.Running -> MaterialTheme.colorScheme.onPrimary
            else -> MaterialTheme.colorScheme.onSurface
        }, label = "run button content color"
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(Modifier.weight(1f)) {
            IconButton(onPickLens, Modifier.size(64.dp)) {
                Icon(Icons.Default.Cameraswitch, "Lens change")
            }

            IconButton(onToggleShow, Modifier.size(64.dp)) {
                AnimatedContent(
                    showPreview,
                    label = "Preview icon",
                    transitionSpec = { fadeIn() togetherWith fadeOut() }) {
                    if (it)
                        Icon(Icons.Default.Visibility, "Hide preview")
                    else
                        Icon(Icons.Default.VisibilityOff, "Show preview")
                }
            }
        }

        Button(
            onStart,
            colors = ButtonDefaults.buttonColors(
                containerColor = startButtonColor,
                contentColor = startButtonContentColor
            ),
            modifier = Modifier.padding(8.dp)
        ) {
            AnimatedContent(serviceStatus, label = "start button text") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (it) {
                        is TrackerService.Status.Starting -> {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        }

                        is TrackerService.Status.Running -> {
                            Icon(imageVector = Icons.Default.Stop, null)
                            Text(stringResource(R.string.stop_button))
                        }

                        else -> {
                            Icon(imageVector = Icons.Default.PlayArrow, null)
                            Text(stringResource(R.string.start_button))
                        }
                    }
                }
            }
        }

        Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
            IconButton(onSettings, Modifier.size(64.dp)) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    }
}

@Preview(widthDp = 400)
@Composable
private fun CameraControlRowPreview() {
    VTrackerTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CameraControlRow(
                TrackerService.Status.NotRunning,
                false,
                {},
                {},
                {},
                {},
                Modifier.fillMaxWidth()
            )
            CameraControlRow(
                TrackerService.Status.Starting,
                false,
                {},
                {},
                {},
                {},
                Modifier.fillMaxWidth()
            )
            CameraControlRow(
                TrackerService.Status.Running,
                false,
                {},
                {},
                {},
                {},
                Modifier.fillMaxWidth()
            )
        }
    }
}