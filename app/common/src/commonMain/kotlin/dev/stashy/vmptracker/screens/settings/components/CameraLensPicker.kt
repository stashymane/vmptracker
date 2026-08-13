package dev.stashy.vmptracker.screens.settings.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.camera.CameraLens
import dev.stashy.vmptracker.camera.LensFacing
import dev.stashy.vmptracker.camera.LensFacing.Back
import dev.stashy.vmptracker.camera.LensFacing.External
import dev.stashy.vmptracker.camera.LensFacing.Front
import dev.stashy.vmptracker.camera.NoOpCameraController
import dev.stashy.vmptracker.icons.Icons
import dev.stashy.vmptracker.icons.outlined.Camera24Dp
import dev.stashy.vmptracker.icons.outlined.CameraVideo24Dp
import dev.stashy.vmptracker.icons.outlined.Check24Dp
import dev.stashy.vmptracker.icons.outlined.MobileCamera24Dp
import dev.stashy.vmptracker.icons.outlined.PhotoCamera24Dp
import dev.stashy.vmptracker.ui.components.cameraLensLabel
import dev.stashy.vmptracker.ui.components.settings.SettingEntry
import dev.stashy.vmptracker.ui.components.settings.SettingsSection
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import org.jetbrains.compose.resources.stringResource
import vmptracker.app.Res
import vmptracker.app.camera_lens_frame_rates
import vmptracker.app.camera_lens_max_resolution

@Composable
fun CameraLensPicker(
    options: List<CameraLens>,
    currentId: String? = null,
    onClick: (String) -> Unit
) = SettingsSection {
    options.forEach { lens ->
        val selected = lens.id == currentId
        val containerColor by animateColorAsState(if (selected) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surfaceContainer)
        val weight by animateIntAsState(if (selected) FontWeight.Black.weight else FontWeight.Normal.weight)

        SettingEntry(
            title = {
                Text(
                    cameraLensLabel(lens),
                    fontWeight = FontWeight(weight),
                )
            },
            subtitle = {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ProvideTextStyle(
                        MaterialTheme.typography.labelSmallEmphasized.copy(fontWeight = Bold)
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
                                if (lens.supportedFrameRates.size > 1)
                                    "${lens.supportedFrameRates.min()}-${lens.supportedFrameRates.max()}"
                                else
                                    "${lens.supportedFrameRates.max()}"
                            )
                        )
                    }
                }
            },
            icon = { LensIcon(lens.facing) },
            onClick = { onClick(lens.id) },
            containerColor = containerColor
        ) {
            if (selected)
                Icon(Icons.Outlined.Check24Dp, null)
        }
    }
}

@Composable
fun LensIcon(facing: LensFacing) = when (facing) {
    Front -> Icon(Icons.Outlined.MobileCamera24Dp, "Front camera")
    Back -> Icon(Icons.Outlined.PhotoCamera24Dp, "Rear camera")
    External -> Icon(Icons.Outlined.CameraVideo24Dp, "External camera")

    else -> Icon(Icons.Outlined.Camera24Dp, "Other camera")
}

@ComponentPreview
@Composable
private fun CameraLensPickerPreview() = PreviewHost {
    val state by NoOpCameraController.lensState.collectAsState()
    CameraLensPicker(state.lenses, state.lenses[1].id) {}
}
