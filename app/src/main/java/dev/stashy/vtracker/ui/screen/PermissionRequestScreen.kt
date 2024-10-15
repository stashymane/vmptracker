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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.stashy.vtracker.R
import dev.stashy.vtracker.ui.theme.VTrackerTheme

@Composable
fun PermissionRequestScreen(
    icon: ImageVector,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    request: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Icon(icon, null, modifier = Modifier.size(64.dp))
        Spacer(Modifier.height(8.dp))

        ProvideTextStyle(MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center)) {
            title()
        }

        ProvideTextStyle(LocalTextStyle.current.copy(textAlign = TextAlign.Center)) {
            description()
        }

        Button(request) {
            Text(stringResource(R.string.camera_required_button))
        }
    }
}

@Preview
@Composable
private fun CameraPermissionScreenPreview() {
    VTrackerTheme {
        PermissionRequestScreen(Icons.Default.Lock, { Text("Title") }, { Text("Desc") }) {}
    }
}