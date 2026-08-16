package dev.stashy.vmptracker.screens.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.stashy.vmptracker.camera.ui.CameraPreviewEffect
import dev.stashy.vmptracker.camera.ui.CameraViewport
import dev.stashy.vmptracker.model.TrackingState.Loading
import dev.stashy.vmptracker.ui.LocalBottomSheetPeekOffset
import dev.stashy.vmptracker.ui.components.CameraControls
import dev.stashy.vmptracker.ui.components.NotificationBar
import dev.stashy.vmptracker.ui.theme.DevicePreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import org.koin.androidx.compose.koinViewModel

@Composable
fun CameraScreen(
    vm: CameraViewmodel = koinViewModel()
) {
    CameraPreviewEffect()

    val peekOffset = LocalBottomSheetPeekOffset.current
    val trackingState by vm.trackingState.collectAsStateWithLifecycle()

    LaunchedEffect(trackingState) { //TODO proper init across app instead of this screen
        if (trackingState == Loading) vm.initialize()
    }

    Box(Modifier.fillMaxSize()) {
        Scaffold(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = -peekOffset.px
                },
            topBar = { NotificationBar() },
            bottomBar = { CameraControls(vm) }
        ) { _ ->
            CameraViewport(Modifier.fillMaxSize())
        }

        StatusEdge(trackingState)
    }
}

@DevicePreview
@Composable
private fun CameraScreenPreview() = PreviewHost {
    CameraScreen(viewModel())
}
