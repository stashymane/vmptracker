package dev.stashy.vmptracker.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.stashy.vmptracker.model.TrackingState
import dev.stashy.vmptracker.ui.LocalDeviceCorners
import dev.stashy.vmptracker.ui.LocalSettings
import dev.stashy.vmptracker.ui.camera.CameraViewport
import dev.stashy.vmptracker.ui.components.CameraControls
import dev.stashy.vmptracker.ui.theme.DevicePreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import dev.stashy.vmptracker.vm.CameraViewmodel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CameraScreen(
    vm: CameraViewmodel = koinViewModel()
) {
    val settings = LocalSettings.current

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {},
        bottomBar = { CameraControls(vm) }
    ) { _ ->
        AnimatedVisibility(
            settings.displayPreview,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CameraViewport()
        }

        val state by vm.trackingState.collectAsStateWithLifecycle()
        StatusEdge(state)
    }
}

@Composable
fun StatusEdge(state: TrackingState, modifier: Modifier = Modifier, radius: Dp = 8.dp) {
    val color by animateColorAsState(
        when (state) {
            is NotRunning -> Color.Transparent
            is Starting -> Color.Yellow
            is Running -> Color.Green
            is Failed -> Color.Red
        }
    )

    val opacityAnimator = remember { Animatable(0f) }
    val opacity by opacityAnimator.asState()

    LaunchedEffect(state) {
        opacityAnimator.stop()
        when (state) {
            is NotRunning -> opacityAnimator.animateTo(0f, tween(500, easing = EaseInOut))

            is Starting -> {
                opacityAnimator.snapTo(0.5f)
                opacityAnimator.animateTo(1f, tween(500, easing = EaseInOut))
                opacityAnimator.animateTo(0.5f, tween(500, easing = EaseInOut))
            }

            is Running -> {
                opacityAnimator.snapTo(0.5f)
                opacityAnimator.animateTo(1f, tween(200, easing = EaseOut))
                opacityAnimator.animateTo(0.5f, tween(2000, easing = EaseInOut))
            }

            is Failed -> {
                opacityAnimator.snapTo(0.5f)
                while (true) {
                    opacityAnimator.animateTo(1f, tween(200, easing = EaseOut))
                    opacityAnimator.animateTo(0.5f, tween(800, easing = EaseInOut))
                }
            }

            else -> opacityAnimator.snapTo(1f)
        }
    }

    val corners = LocalDeviceCorners.current
    val radius = with(LocalDensity.current) { radius.toPx() }

    Box(
        modifier.fillMaxSize().drawWithContent {
            drawContent()

            val stroke = Stroke(radius)
            corners?.let { corners ->
                drawPath(corners, color, alpha = opacity, style = stroke)
            } ?: drawRect(color, size = size, alpha = opacity, style = stroke)
        })
}

@DevicePreview
@Composable
private fun CameraScreenPreview() = PreviewHost {
    CameraScreen(viewModel())
}
