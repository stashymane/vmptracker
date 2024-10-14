package dev.stashy.vtracker.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun MainScreen(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewmodel: MainViewmodel = koinInject()

) {
    val navController = LocalNavController.current
    val sidePadding = 16.dp

    val trackerService by viewmodel.tracker.collectAsState()

    AnimatedContent(
        trackerService,
        label = "service loading"
    ) { tracker ->
        when (tracker) {
            null -> {
                LoadingScreen()
            }

            else -> {
                val status by tracker.status.collectAsState()

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        //camera view
                    }

                    Row(
                        modifier = Modifier.padding(horizontal = sidePadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedContent(status, label = "is running") {
                            when (it) {
                                TrackerService.Status.Running -> Text(
                                    stringResource(R.string.service_running),
                                    color = MaterialTheme.colorScheme.primary
                                )

                                else -> Text(
                                    stringResource(R.string.service_notrunning),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        Spacer(Modifier.weight(1f))

                        AnimatedContent(status, label = "run button") {
                            when (it) {
                                TrackerService.Status.Running -> TextButton(
                                    viewmodel::stop,
                                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Icon(imageVector = Icons.Default.Close, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text(stringResource(R.string.stop_button))
                                }


                                else -> Button(viewmodel::start) {
                                    Icon(imageVector = Icons.Default.PlayArrow, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text(stringResource(R.string.start_button))
                                }
                            }
                        }
                    }

                    Row(modifier = Modifier.padding(horizontal = sidePadding)) {
                        TextButton(
                            { navController.navigate(Screen.Settings) },
                            modifier = Modifier.weight(1f),
                            enabled = status == TrackerService.Status.NotRunning
                        ) {
                            Icon(Icons.Default.Settings, null)
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.settings_title))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    VTrackerTheme {
        MainScreen()
    }
}