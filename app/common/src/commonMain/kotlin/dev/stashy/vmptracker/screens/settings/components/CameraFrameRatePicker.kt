package dev.stashy.vmptracker.screens.settings.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.stashy.vmptracker.ui.components.RadioButtonRow
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
    RadioButtonRow(options, current, onSelect, modifier) {
        Text(stringResource(Res.string.settings_capture_framerate_value, it))
    }
}

@ComponentPreview
@Composable
private fun CameraFrameRatePickerPreview() = PreviewHost {
    CameraFrameRatePicker(listOf(30, 60, 120), 60, {})
}
