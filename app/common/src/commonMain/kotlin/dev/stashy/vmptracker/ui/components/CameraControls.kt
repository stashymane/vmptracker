package dev.stashy.vmptracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.stashy.vmptracker.ui.LocalSettingsActions
import dev.stashy.vmptracker.ui.LocalSnackbarState
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import dev.stashy.vmptracker.vm.CameraViewmodel
import org.jetbrains.compose.resources.stringResource
import vmptracker.app.Res
import vmptracker.app.error_tracking_failed
import vmptracker.app.tracking_button_retry

@Composable
fun CameraControls(vm: CameraViewmodel, modifier: Modifier = Modifier) {
    val snackbarState = LocalSnackbarState.current

    val state by vm.trackingState.collectAsStateWithLifecycle()

    val errorText = stringResource(Res.string.error_tracking_failed)
    val retryButtonText = stringResource(Res.string.tracking_button_retry)
    LaunchedEffect(state) {
        val state = state as? Failed ?: return@LaunchedEffect

        val result = snackbarState.showSnackbar(
            message = "$errorText: ${state.reason}",
            actionLabel = retryButtonText,
            withDismissAction = true,
            duration = SnackbarDuration.Indefinite
        )

        when (result) {
            ActionPerformed -> vm.startTracking()
            Dismissed -> vm.stopTracking()
        }
    }

    Box(
        modifier.fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeContent)
            .padding(bottom = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SnackbarHost(LocalSnackbarState.current) { data ->
                Snackbar(data)
            }

            CameraControlBar(vm)
        }
    }
}

@ComponentPreview
@Composable
private fun CameraControlsPreview() = PreviewHost {
    CameraControls(viewModel())
}
