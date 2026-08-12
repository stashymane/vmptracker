package dev.stashy.vmptracker.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.stashy.vmptracker.icons.Icons
import dev.stashy.vmptracker.icons.filled.PlayArrow24Dp
import dev.stashy.vmptracker.icons.filled.Stop24Dp
import dev.stashy.vmptracker.icons.outlined.Settings24Dp
import dev.stashy.vmptracker.icons.outlined.Visibility24Dp
import dev.stashy.vmptracker.icons.outlined.VisibilityOff24
import dev.stashy.vmptracker.model.Screens
import dev.stashy.vmptracker.model.TrackingState
import dev.stashy.vmptracker.ui.LocalBackStack
import dev.stashy.vmptracker.ui.LocalSettings
import dev.stashy.vmptracker.ui.LocalSettingsActions
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import dev.stashy.vmptracker.vm.CameraViewmodel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vmptracker.app.Res
import vmptracker.app.screen_title_settings
import vmptracker.app.tracking_button_start
import vmptracker.app.tracking_button_stop
import vmptracker.app.visibility_hidden
import vmptracker.app.visibility_visible

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CameraControlBar(vm: CameraViewmodel, modifier: Modifier = Modifier) {
    val settingsActions = LocalSettingsActions.current
    val settings = LocalSettings.current
    val backStack = LocalBackStack.current
    val motionScheme = MaterialTheme.motionScheme

    val scope = rememberCoroutineScope()

    val state by vm.trackingState.collectAsStateWithLifecycle()

    Row(
        modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilledIconToggleButton(settings.displayPreview, {
            scope.launch { settingsActions.togglePreviewVisibility() }
        }) {
            AnimatedContent(
                settings.displayPreview,
                transitionSpec = {
                    fadeIn(motionScheme.fastSpatialSpec()) togetherWith fadeOut(
                        motionScheme.fastSpatialSpec()
                    )
                }
            ) { displayPreview ->
                if (displayPreview)
                    Icon(
                        Icons.Outlined.Visibility24Dp,
                        stringResource(Res.string.visibility_visible)
                    )
                else
                    Icon(
                        Icons.Outlined.VisibilityOff24,
                        stringResource(Res.string.visibility_hidden)
                    )
            }
        }

        Button(
            vm::toggleTracking,
            Modifier.widthIn(min = 160.dp),
            enabled = state !is TrackingState.Starting && state !is TrackingState.Failed
        ) {
            AnimatedContent(
                state,
                transitionSpec = {
                    fadeIn(motionScheme.slowSpatialSpec()) togetherWith fadeOut(motionScheme.slowSpatialSpec()) using null
                },
                contentAlignment = Alignment.Center
            ) {
                when (it) {
                    TrackingState.NotRunning, is TrackingState.Failed -> StartButtonContent(
                        Icons.Filled.PlayArrow24Dp,
                        stringResource(Res.string.tracking_button_start)
                    )

                    TrackingState.Starting -> Box(Modifier.wrapContentSize()) {
                        LinearProgressIndicator(Modifier.width(80.dp))
                    }

                    TrackingState.Running -> StartButtonContent(
                        Icons.Filled.Stop24Dp,
                        stringResource(Res.string.tracking_button_stop)
                    )
                }
            }
        }

        IconButton({
            backStack.add(Screens.Settings)
        }) {
            Icon(Icons.Outlined.Settings24Dp, stringResource(Res.string.screen_title_settings))
        }
    }
}

@Composable
fun StartButtonContent(icon: ImageVector, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, text)
        Text(text)
    }
}

@ComponentPreview
@Composable
private fun CameraControlBarPreview() = PreviewHost {
    CameraControlBar(viewModel())
}
