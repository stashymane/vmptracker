package dev.stashy.vtracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.mediapipe.tasks.core.Delegate
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.stashy.vtracker.R
import dev.stashy.vtracker.ui.LocalHazeStyle
import dev.stashy.vtracker.ui.component.LocalNavController
import dev.stashy.vtracker.ui.screen.parts.ConnectionSettingsDialog
import dev.stashy.vtracker.ui.screen.parts.ConnectionSettingsPart
import dev.stashy.vtracker.ui.screen.parts.FaceSettingsDialog
import dev.stashy.vtracker.ui.screen.parts.FaceSettingsPart
import dev.stashy.vtracker.ui.screen.parts.HandSettingsDialog
import dev.stashy.vtracker.ui.screen.parts.HandSettingsPart
import dev.stashy.vtracker.ui.vm.SettingsViewmodel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    vm: SettingsViewmodel = koinViewModel()
) {
    val scrollState = rememberScrollState()
    val navController = LocalNavController.current

    val hazeState = remember { HazeState() }
    val hazeStyle = HazeDefaults.style(MaterialTheme.colorScheme.surfaceDim)

    val surfaceGradient =
        listOf(
            MaterialTheme.colorScheme.surface.copy(0f),
            MaterialTheme.colorScheme.surface
        )

    val connectionState by vm.connectionState.collectAsState()
    val faceTrackingState by vm.faceTrackingState.collectAsState()
    val handTrackingState by vm.handTrackingState.collectAsState()

    Box(
        Modifier
            .fillMaxSize()
    ) {
        Box(
            Modifier
                .padding(contentPadding)
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
                    TextButton(onClick = vm::reset) { Text(stringResource(R.string.settings_reset)) }
                }

                ConnectionSettingsPart(connectionState)
                FaceSettingsPart(faceTrackingState)
                HandSettingsPart(handTrackingState)
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
            modifier = Modifier
                .clipToBounds()
                .hazeChild(hazeState, LocalHazeStyle.current)
                .background(Brush.verticalGradient(surfaceGradient))
                .padding(
                    start = 16.dp + contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = 16.dp + contentPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = 8.dp,
                    bottom = 8.dp + contentPadding.calculateBottomPadding()
                )
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

    val dialogModifier = Modifier.hazeChild(hazeState, hazeStyle)

    ConnectionSettingsDialog(connectionState, dialogModifier)
    FaceSettingsDialog(faceTrackingState, dialogModifier)
    HandSettingsDialog(handTrackingState, dialogModifier)
}

@Composable
fun TitleRow(titleId: Int, icon: ImageVector) {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null)
        Text(
            stringResource(titleId),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

fun Delegate.stringResource() = when (this) {
    Delegate.CPU -> R.string.settings_runner_CPU
    Delegate.GPU -> R.string.settings_runner_GPU
}

fun Float.toPercentage() = "${(this * 100).toInt()}%"