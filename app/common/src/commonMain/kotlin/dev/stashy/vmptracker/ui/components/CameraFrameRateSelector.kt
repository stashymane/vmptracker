package dev.stashy.vmptracker.ui.components

import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import dev.stashy.vmptracker.model.Screens
import dev.stashy.vmptracker.ui.LocalBackStack
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost

@Composable
fun CameraFrameRateSelector(
    frameRate: Int,
    modifier: Modifier = Modifier,
) {
    val backStack = LocalBackStack.current

    IconButton(
        onClick = { backStack.add(Screens.CameraFrameRatePicker) },
        modifier,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.75f)
        )
    ) {
        Text(
            "$frameRate",
            color = Color.White,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@ComponentPreview
@Composable
private fun CameraFrameRateSelectorPreview() = PreviewHost {
    CameraFrameRateSelector(frameRate = 60)
}
