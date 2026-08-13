package dev.stashy.vmptracker.screens.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.stashy.vmptracker.screens.camera.CameraViewmodel
import dev.stashy.vmptracker.screens.settings.components.CameraLensPicker
import dev.stashy.vmptracker.ui.components.settings.SettingsSection
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import vmptracker.app.Res
import vmptracker.app.camera_lens_selector

@Composable
fun CameraLensPickerSheet(vm: CameraViewmodel = koinViewModel()) {
    val lensState by vm.lensState.collectAsStateWithLifecycle()

    Column(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SettingsSection({
            Text(stringResource(Res.string.camera_lens_selector))
        }) {
            CameraLensPicker(lensState.lenses, lensState.selectedId) {
                vm.selectLens(it)
            }
        }
    }
}

@ComponentPreview
@Composable
private fun CameraLensPickerSheetPreview() = PreviewHost {
    val vm = remember { CameraViewmodel() }
    CameraLensPickerSheet(vm)
}
