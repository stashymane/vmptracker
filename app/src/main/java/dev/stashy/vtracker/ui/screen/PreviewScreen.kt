package dev.stashy.vtracker.ui.screen

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.viewfinder.surface.ImplementationMode
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.stashy.vtracker.model.settings.GeneralSettings
import dev.stashy.vtracker.service.TrackerService
import dev.stashy.vtracker.ui.component.CameraControlRow
import dev.stashy.vtracker.ui.component.LocalNavController
import dev.stashy.vtracker.ui.component.dialog.CameraChoiceContent
import dev.stashy.vtracker.ui.theme.VTrackerTheme
import dev.stashy.vtracker.ui.vm.MainViewmodel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    contentPadding: PaddingValues,
    vm: MainViewmodel = koinInject()
) {
    val status by vm.status.collectAsState()
    val generalSettings by vm.generalSettings.data.collectAsState(GeneralSettings())
    val surfaceRequest by vm.surfaceRequests.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    val navController = LocalNavController.current

    var showCameraPicker by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LifecycleStartEffect(generalSettings.cameraId) {
        vm.startPreview(lifecycleOwner, generalSettings.cameraId ?: "1", 0)
        onStopOrDispose {
            vm.stopPreview()
        }
    }

    Box(Modifier.fillMaxSize()) {
        surfaceRequest?.let {
            CameraXViewfinder(
                it,
                implementationMode = ImplementationMode.EMBEDDED,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            CameraControlRow(modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
                isActive = status is TrackerService.Status.Running,
                onPickLens = { showCameraPicker = true },
                onStart = {
                    if (status is TrackerService.Status.Running) vm.stop()
                    else vm.start()
                },
                onSettings = { navController.navigate(Screen.Settings) })
        }
    }

    if (showCameraPicker)
        ModalBottomSheet({ showCameraPicker = false }, sheetState = bottomSheetState) {
            CameraChoiceContent(
                vm,
                generalSettings.cameraId ?: "1",
                { showCameraPicker = false }, vm::switchCamera
            )
        }
}

@Preview
@Composable
private fun PreviewScreenPreview() {
    VTrackerTheme {
        PreviewScreen(PaddingValues())
    }
}