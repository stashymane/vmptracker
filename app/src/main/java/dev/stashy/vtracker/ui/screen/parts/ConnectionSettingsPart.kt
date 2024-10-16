package dev.stashy.vtracker.ui.screen.parts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.stashy.vtracker.R
import dev.stashy.vtracker.ui.component.SettingsRow
import dev.stashy.vtracker.ui.component.dialog.ADialog
import dev.stashy.vtracker.ui.component.dialog.IpAddressDialogContent
import dev.stashy.vtracker.ui.screen.TitleRow
import dev.stashy.vtracker.ui.theme.VTrackerTheme
import dev.stashy.vtracker.ui.vm.SettingsViewmodel

@Composable
fun ConnectionSettingsPart(
    state: SettingsViewmodel.ConnectionState
) {
    var ipAddress by state.ipAddress
    var ipAddressDialogVisible by state.ipAddressDialogVisible
    
    TitleRow(R.string.settings_category_connection, Icons.Default.SignalCellularAlt)

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
}

@Composable
fun ConnectionSettingsDialog(state: SettingsViewmodel.ConnectionState, modifier: Modifier) {
    var ipAddress by state.ipAddress
    var ipAddressDialogVisible by state.ipAddressDialogVisible
    val onDismiss = { ipAddressDialogVisible = false }

    ADialog(
        ipAddressDialogVisible, onDismiss,
        modifier = modifier
    ) {
        IpAddressDialogContent(
            onDismiss,
            currentAddress = ipAddress
        ) { ipAddress = it }
    }
}

@Preview
@Composable
private fun ConnectionSettingsPartPreview() {
    VTrackerTheme {
        ConnectionSettingsPart(SettingsViewmodel.ConnectionState())
    }
}
