package dev.stashy.vtracker.ui.screen.parts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.mediapipe.tasks.core.Delegate
import dev.stashy.vtracker.R
import dev.stashy.vtracker.ui.component.SettingsRow
import dev.stashy.vtracker.ui.component.dialog.ADialog
import dev.stashy.vtracker.ui.component.dialog.ListDialogContent
import dev.stashy.vtracker.ui.screen.TitleRow
import dev.stashy.vtracker.ui.screen.stringResource
import dev.stashy.vtracker.ui.screen.toPercentage
import dev.stashy.vtracker.ui.theme.VTrackerTheme
import dev.stashy.vtracker.ui.vm.SettingsViewmodel

@Composable
fun HandSettingsPart(state: SettingsViewmodel.HandTrackingState) {
    var runner by state.runner
    var detectionConfidence by state.detectionConfidence
    var trackingConfidence by state.trackingConfidence
    var presenceConfidence by state.presenceConfidence

    var runnerDialogVisible by state.runnerDialogVisible

    TitleRow(R.string.settings_category_hand_tracking, Icons.Default.WavingHand)

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

@Composable
fun HandSettingsDialog(state: SettingsViewmodel.HandTrackingState, modifier: Modifier) {
    var runnerDialogVisible by state.runnerDialogVisible
    var runner by state.runner

    ADialog(
        runnerDialogVisible,
        { runnerDialogVisible = false },
        modifier = modifier
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

@Preview
@Composable
private fun HandSettingsPartPreview() {
    VTrackerTheme {
        HandSettingsPart(SettingsViewmodel.HandTrackingState())
    }
}