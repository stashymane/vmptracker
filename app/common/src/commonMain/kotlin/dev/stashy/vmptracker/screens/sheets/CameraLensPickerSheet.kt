package dev.stashy.vmptracker.screens.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.stashy.vmptracker.camera.CameraLens
import dev.stashy.vmptracker.screens.camera.CameraViewmodel
import dev.stashy.vmptracker.ui.LocalBackStack
import dev.stashy.vmptracker.ui.components.cameraLensLabel
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import vmptracker.app.Res
import vmptracker.app.camera_lens_frame_rates
import vmptracker.app.camera_lens_max_resolution
import vmptracker.app.camera_lens_selector

@Composable
fun CameraLensPickerSheet(vm: CameraViewmodel = koinViewModel()) {
    val backStack = LocalBackStack.current
    val lensState by vm.lensState.collectAsStateWithLifecycle()

    Column(
        Modifier.fillMaxWidth().padding(bottom = 64.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            stringResource(Res.string.camera_lens_selector),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        )

        LazyColumn {
            items(lensState.lenses, key = CameraLens::id) { lens ->
                val selected = lens.id == lensState.selectedId

                ListItem(
                    headlineContent = {
                        Text(
                            cameraLensLabel(lens),
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        )
                    },
                    supportingContent = {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            ProvideTextStyle(
                                MaterialTheme.typography.labelSmallEmphasized.copy(
                                    fontWeight = FontWeight.Bold,
                                )
                            ) {
                                Text(
                                    stringResource(
                                        Res.string.camera_lens_max_resolution,
                                        lens.maxVideoWidth,
                                        lens.maxVideoHeight
                                    )
                                )

                                Text(
                                    stringResource(
                                        Res.string.camera_lens_frame_rates,
                                        lens.supportedFrameRates.max()
                                    )
                                )
                            }
                        }
                    },
                    leadingContent = {
                        RadioButton(
                            selected = selected,
                            onClick = null,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            vm.selectLens(lens.id)
                            backStack.removeLast()
                        },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
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
