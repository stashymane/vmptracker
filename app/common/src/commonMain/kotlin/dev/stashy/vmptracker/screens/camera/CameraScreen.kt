package dev.stashy.vmptracker.screens.camera

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.stashy.vmptracker.camera.ui.CameraPreviewEffect
import dev.stashy.vmptracker.camera.ui.CameraViewport
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

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = { NotificationBar() },
        bottomBar = { CameraControls(vm) }
    ) { _ ->
        CameraViewport(Modifier.fillMaxSize())

        val state by vm.trackingState.collectAsStateWithLifecycle()
        StatusEdge(state)
    }
}

@DevicePreview
@Composable
private fun CameraScreenPreview() = PreviewHost {
    CameraScreen(viewModel())
}
