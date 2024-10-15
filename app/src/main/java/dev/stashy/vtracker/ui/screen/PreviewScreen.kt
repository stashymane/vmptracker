package dev.stashy.vtracker.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.stashy.vtracker.R
import dev.stashy.vtracker.service.TrackerService
import dev.stashy.vtracker.ui.component.LocalNavController
import dev.stashy.vtracker.ui.theme.VTrackerTheme
import dev.stashy.vtracker.ui.vm.MainViewmodel
import org.koin.compose.koinInject

@Composable
fun PreviewScreen(
    status: TrackerService.Status,
    contentPadding: PaddingValues,
    viewmodel: MainViewmodel = koinInject()
) {
    val navController = LocalNavController.current
    val displayMetrics = LocalContext.current.resources.displayMetrics

    val startButtonColor by animateColorAsState(
        when (status) {
            is TrackerService.Status.Running -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surfaceContainer
        }, label = "run button color"
    )
    val startButtonContentColor by animateColorAsState(
        when (status) {
            is TrackerService.Status.Running -> MaterialTheme.colorScheme.onPrimary
            else -> MaterialTheme.colorScheme.onSurface
        }, label = "run button content color"
    )

    Box(Modifier.fillMaxSize()) {
        //TODO viewfinder should be here

        Box(
            Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            Button(
                {
                    if (viewmodel.status.value is TrackerService.Status.Running) viewmodel.stop()
                    else viewmodel.start()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = startButtonColor,
                    contentColor = startButtonContentColor
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp)
            ) {
                AnimatedContent(status, label = "start button text") {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when (it) {
                            is TrackerService.Status.Running -> {
                                Icon(imageVector = Icons.Default.Close, null)
                                Text(stringResource(R.string.stop_button))
                            }

                            is TrackerService.Status.NotRunning,
                            is TrackerService.Status.Error -> {
                                Icon(imageVector = Icons.Default.PlayArrow, null)
                                Text(stringResource(R.string.start_button))
                            }
                        }
                    }
                }
            }

            IconButton(
                { navController.navigate(Screen.Settings) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(32.dp)
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewScreenPreview() {
    VTrackerTheme {
        PreviewScreen(TrackerService.Status.Running, PaddingValues())
    }
}