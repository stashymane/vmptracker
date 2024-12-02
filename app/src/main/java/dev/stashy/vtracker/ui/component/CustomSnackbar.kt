package dev.stashy.vtracker.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.stashy.vtracker.ui.theme.VTrackerTheme

@Composable
fun CustomSnackbar(
    content: @Composable () -> Unit,
    action: @Composable () -> Unit,
    dismissAction: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Snackbar(
        modifier = modifier,
        content = content,
        action = action,
        dismissAction = dismissAction,
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        actionContentColor = MaterialTheme.colorScheme.primary,
        dismissActionContentColor = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun CustomSnackbar(data: SnackbarData, modifier: Modifier = Modifier) {
    CustomSnackbar(
        modifier = modifier,
        content = { Text(data.visuals.message) },
        action = {
            data.visuals.actionLabel?.let { actionLabel ->
                Button(data::performAction) { Text(actionLabel) }
            }
        },
        dismissAction = {
            if (data.visuals.withDismissAction)
                IconButton(data::dismiss) {
                    Icon(Icons.Default.Close, "Dismiss message")
                }
        }
    )
}

@Preview
@Composable
private fun CustomSnackbarPreview() {
    VTrackerTheme {
        CustomSnackbar(
            content = {
                Text("Snackbar content")
            },
            action = {
                TextButton({}) {
                    Text("Action")
                }
            },
            dismissAction = {
                TextButton({}) {
                    Text("Dismiss")
                }
            })
    }
}