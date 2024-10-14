package dev.stashy.vtracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.mediapipe.tasks.core.Delegate
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.stashy.vtracker.R
import dev.stashy.vtracker.model.IpAddress
import dev.stashy.vtracker.ui.LocalHazeStyle
import dev.stashy.vtracker.ui.component.LocalNavController
import dev.stashy.vtracker.ui.component.SettingsRow
import dev.stashy.vtracker.ui.component.dialog.ADialog
import dev.stashy.vtracker.ui.component.dialog.CameraChoiceContent
import dev.stashy.vtracker.ui.component.dialog.IpAddressDialogContent
import dev.stashy.vtracker.ui.component.dialog.ListDialogContent

@Composable
fun SettingsScreen(contentPadding: PaddingValues = PaddingValues(0.dp)) {
    val scrollState = rememberScrollState()
    val navController = LocalNavController.current
    val hazeState = remember { HazeState() }
    val surfaceGradient =
        listOf(
            MaterialTheme.colorScheme.surface.copy(0f),
            MaterialTheme.colorScheme.surface
        )

    var ipAddress by remember { mutableStateOf(IpAddress("192.168.1.10", 5123)) }

    var cameraId by remember { mutableStateOf("0") }
    var runner by remember { mutableStateOf(Delegate.GPU) }
    var detectionConfidence by remember { mutableFloatStateOf(0.5f) }
    var trackingConfidence by remember { mutableFloatStateOf(0.5f) }
    var presenceConfidence by remember { mutableFloatStateOf(0.5f) }

    var cameraChoiceDialogVisible by remember { mutableStateOf(false) }
    var ipAddressDialogVisible by remember { mutableStateOf(false) }
    var runnerDialogVisible by remember { mutableStateOf(false) }

    Box(
        Modifier
            .padding(contentPadding)
            .fillMaxSize()
    ) {
        Box(
            Modifier
                .haze(hazeState)
                .matchParentSize()
        ) {
            Column(
                Modifier
                    .verticalScroll(scrollState)
                    .matchParentSize()
                    .padding(bottom = 64.dp)
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(R.string.settings_title),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = {}) { Text(stringResource(R.string.settings_reset)) }
                }

                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Share, null)
                    Text(
                        stringResource(R.string.settings_category_connection),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                SettingsRow(
                    title = { Text(stringResource(R.string.setting_targetip_title)) },
                    description = { Text(stringResource(R.string.setting_targetip_description)) },
                    current = { Text(ipAddress.toString()) }) {
                    ipAddressDialogVisible = true
                }

                SettingsRow(
                    enabled = false,
                    title = { Text(stringResource(R.string.setting_protocol_title)) },
                    description = { Text(stringResource(R.string.setting_protocol_description)) },
                    current = { Text("VTracker") }) {}

                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Face, null)
                    Text(
                        stringResource(R.string.settings_category_face_tracking),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                SettingsRow(
                    enabled = false,
                    title = { Text(stringResource(R.string.setting_tracking_model_title)) },
                    description = { Text(stringResource(R.string.setting_tracking_model_description)) },
                    current = { Text(stringResource(R.string.setting_tracking_model_mediapipe)) }) {}
                SettingsRow(
                    title = { Text(stringResource(R.string.setting_camera_title)) },
                    description = { Text(stringResource(R.string.setting_camera_description)) },
                    current = { Text(stringResource(R.string.setting_camera_back) + " 1") }) {
                    cameraChoiceDialogVisible = true
                }
                SettingsRow(
                    title = { Text(stringResource(R.string.setting_runner_title)) },
                    description = { Text(stringResource(R.string.setting_runner_description)) },
                    current = { Text(stringResource(runner.stringResource())) }) {
                    runnerDialogVisible = true
                }
                SettingsRow(
                    title = { Text(stringResource(R.string.setting_detectionconf_title)) },
                    description = { Text(stringResource(R.string.setting_detectionconf_description)) },
                    current = { Text(detectionConfidence.toPercentage()) },
                    control = {
                        Slider(detectionConfidence, { detectionConfidence = it })
                    }) {}
                SettingsRow(
                    title = { Text(stringResource(R.string.setting_trackingconf_title)) },
                    description = { Text(stringResource(R.string.setting_trackingconf_description)) },
                    current = { Text(trackingConfidence.toPercentage()) },
                    control = {
                        Slider(trackingConfidence, { trackingConfidence = it })
                    }) {}
                SettingsRow(
                    title = { Text(stringResource(R.string.setting_presenceconf_title)) },
                    description = { Text(stringResource(R.string.setting_presenceconf_desription)) },
                    current = { Text(presenceConfidence.toPercentage()) },
                    control = {
                        Slider(presenceConfidence, { presenceConfidence = it })
                    }) {}
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
            modifier = Modifier
                .clipToBounds()
                .hazeChild(hazeState, LocalHazeStyle.current)
                .background(Brush.verticalGradient(surfaceGradient))
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .align(Alignment.BottomStart)
        ) {
            TextButton({ navController.popBackStack() }) {
                Icon(Icons.Default.Delete, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.settings_discard))
            }

            Button({}) {
                Icon(Icons.Default.Done, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.settings_save))
            }
        }
    }

    ADialog(
        cameraChoiceDialogVisible,
        { cameraChoiceDialogVisible = false },
        modifier = Modifier.hazeChild(hazeState, LocalHazeStyle.current)
    ) {
        CameraChoiceContent(
            { cameraChoiceDialogVisible = false },
            { cameraId = it }
        )
    }

    ADialog(
        ipAddressDialogVisible, { ipAddressDialogVisible = false },
        modifier = Modifier.hazeChild(hazeState, LocalHazeStyle.current)
    ) {
        IpAddressDialogContent(
            { ipAddressDialogVisible = false },
            currentAddress = ipAddress
        ) { ipAddress = it }
    }

    ADialog(
        runnerDialogVisible,
        { runnerDialogVisible = false },
        modifier = Modifier.hazeChild(hazeState, LocalHazeStyle.current)
    ) {
        ListDialogContent(
            items = Delegate.entries.toList(),
            title = { Text(stringResource(R.string.setting_runner_title)) },
            onDismiss = { runnerDialogVisible = false },
            onSelect = { runner = it }
        ) {
            Text(it.name)
        }
    }
}

private fun Delegate.stringResource() = when (this) {
    Delegate.CPU -> R.string.settings_runner_CPU
    Delegate.GPU -> R.string.settings_runner_GPU
}

private fun Float.toPercentage() = "${(this * 100).toInt()}%"