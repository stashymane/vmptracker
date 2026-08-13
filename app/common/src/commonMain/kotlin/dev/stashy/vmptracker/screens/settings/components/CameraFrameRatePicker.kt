package dev.stashy.vmptracker.screens.settings.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.TonalToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import org.jetbrains.compose.resources.stringResource
import vmptracker.app.Res
import vmptracker.app.settings_capture_framerate_value

@Composable
fun CameraFrameRatePicker(
    options: List<Int>,
    current: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        options.forEachIndexed { index, fps ->
            val selected = fps == current
            val weight by animateIntAsState(if (selected) FontWeight.Black.weight else FontWeight.Normal.weight)

            TonalToggleButton(
                selected,
                { onSelect(fps) },
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    options.size - 1 -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                }
            ) {
                Text(
                    stringResource(Res.string.settings_capture_framerate_value, fps),
                    fontWeight = FontWeight(weight)
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun CameraFrameRatePickerPreview() = PreviewHost {
    CameraFrameRatePicker(listOf(30, 60, 120), 60, {})
}
