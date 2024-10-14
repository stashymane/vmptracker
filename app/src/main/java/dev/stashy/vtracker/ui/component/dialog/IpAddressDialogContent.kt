package dev.stashy.vtracker.ui.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.stashy.vtracker.model.IpAddress
import dev.stashy.vtracker.ui.theme.VTrackerTheme

@Composable
fun IpAddressDialogContent(
    onDismiss: () -> Unit,
    currentAddress: IpAddress = IpAddress("127.0.0.1", 5123),
    onSave: (IpAddress) -> Unit
) {
    var address by remember { mutableStateOf(currentAddress.address) }
    var port by remember { mutableIntStateOf(currentAddress.port) }

    val isAddressValid = IpAddress.validateIp(address)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            stringResource(dev.stashy.vtracker.R.string.setting_targetip_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                address,
                { address = it },
                label = { Text("IP Address") },
                singleLine = true,
                modifier = Modifier.weight(2f),
                isError = !isAddressValid
            )
            OutlinedTextField(
                port.toString(),
                { port = it.toIntOrNull() ?: port },
                label = { Text("Port") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton({ onDismiss() }) {
                Text("Cancel")
            }

            Button(
                {
                    onSave(IpAddress(address, port))
                    onDismiss()
                },
                enabled = isAddressValid
            ) {
                Text("Save")
            }
        }
    }
}

@Preview
@Composable
private fun IpAddressDialogPreview() {
    VTrackerTheme {
        IpAddressDialogContent({}) {}
    }
}