package dev.stashy.vtracker.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.stashy.vtracker.ui.component.LocalNavController
import dev.stashy.vtracker.ui.theme.VTrackerTheme
import dev.stashy.vtracker.ui.vm.MainViewmodel

@Composable
fun MainScreen(contentPadding: PaddingValues = PaddingValues(0.dp)) {
    val viewmodel = viewModel<MainViewmodel>()
    val fps by viewmodel.fps.collectAsState()
    val isRunning by viewmodel.isRunning.collectAsState()

    val navController = LocalNavController.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                //camera view
            }
        }

        item {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(8.dp))
                AnimatedContent(isRunning, label = "is running") {
                    when (it) {
                        true -> Text("Running", color = MaterialTheme.colorScheme.primary)
                        false -> Text("Not running", color = MaterialTheme.colorScheme.error)
                    }
                }

                Spacer(Modifier.width(32.dp))
                Text("FPS $fps")
                Spacer(Modifier.weight(1f))

                AnimatedContent(isRunning, label = "run button") {
                    when (it) {
                        false -> {
                            Button(viewmodel::start) {
                                Icon(imageVector = Icons.Default.PlayArrow, null)
                                Spacer(Modifier.width(8.dp))
                                Text(stringResource(dev.stashy.vtracker.R.string.start_button))
                            }
                        }

                        true -> {
                            TextButton(
                                viewmodel::stop,
                                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                            ) {
                                Icon(imageVector = Icons.Default.Close, null)
                                Spacer(Modifier.width(8.dp))
                                Text(stringResource(dev.stashy.vtracker.R.string.stop_button))
                            }
                        }
                    }
                }
            }
        }

        item {
            Row {
                TextButton(
                    { navController.navigate(Screen.Settings) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Settings, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Settings")
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