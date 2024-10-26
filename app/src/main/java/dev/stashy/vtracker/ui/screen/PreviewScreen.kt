package dev.stashy.vtracker.ui.screen

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.viewfinder.surface.ImplementationMode
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
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
import dev.stashy.vtracker.ui.component.dialog.ADialog
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

    LifecycleStartEffect(generalSettings) {
        if (generalSettings.displayPreview)
            vm.startPreview(lifecycleOwner, generalSettings.cameraId ?: "1")
        else
            vm.stopPreview()

        onStopOrDispose {
            vm.stopPreview()
        }
    }

    Box(Modifier.fillMaxSize()) {
        surfaceRequest?.let {
            AnimatedVisibility(generalSettings.displayPreview, enter = fadeIn(), exit = fadeOut()) {
                CameraXViewfinder(
                    it,
                    implementationMode = ImplementationMode.EMBEDDED,
                    modifier = Modifier.fillMaxSize()
                )
            }
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
                showPreview = generalSettings.displayPreview,
                onPickLens = { showCameraPicker = true },
                onToggleShow = { vm.showPreview(!generalSettings.displayPreview) },
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

    (status as? TrackerService.Status.Error)?.let { error ->
        val dismiss = { vm.stop() }
        ADialog(true, dismiss) {
            Column(modifier = Modifier.padding(32.dp)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .verticalScroll(
                            rememberScrollState()
                        )
                        .fillMaxSize()
                ) {
                    Text("An error has occurred.", style = MaterialTheme.typography.titleLarge)

                    Column {
                        Text(
                            error.exception::class.simpleName ?: "Unable to retrieve class name"
                        )
                        Text(error.exception.message ?: "No message provided")
                    }

                    error.exception.cause?.let { cause ->
                        Column {
                            Text("Caused by ${cause::class.simpleName ?: "unknown"}")
                            cause.message?.let {
                                Text(it)
                            }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Button(dismiss) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewScreenPreview() {
    VTrackerTheme {
        PreviewScreen(PaddingValues())
    }
}