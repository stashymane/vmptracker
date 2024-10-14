package dev.stashy.vtracker.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.stashy.vtracker.R
import dev.stashy.vtracker.ui.theme.VTrackerTheme

@Composable
fun PermissionRequestScreen(modifier: Modifier = Modifier, request: () -> Unit = {}) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Icon(Icons.Default.Lock, null, modifier = Modifier.size(64.dp))
        Spacer(Modifier.height(8.dp))

        Text(
            stringResource(R.string.camera_required_title),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(stringResource(R.string.camera_required_description), textAlign = TextAlign.Center)

        Button(request) {
            Text(stringResource(R.string.camera_required_button))
        }
    }
}

@Preview
@Composable
private fun CameraPermissionScreenPreview() {
    VTrackerTheme {
        PermissionRequestScreen()
    }
}