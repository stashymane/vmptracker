package dev.stashy.vtracker.ui.component.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dev.stashy.vtracker.ui.theme.VTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ADialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    backgroundColor: Color = Color.Transparent,
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit
) {
    val animationSpec = spring<Float>(stiffness = Spring.StiffnessMedium)

    AnimatedVisibility(visible, enter = fadeIn(animationSpec), exit = fadeOut(animationSpec)) {
        Box(
            modifier
                .fillMaxSize()
                .background(backgroundColor)
        )
    }

    if (visible)
        BasicAlertDialog(onDismissRequest = onDismiss) {
            Surface(
                shape = shape,
                color = containerColor
            ) {
                content()
            }
        }
}

@Preview
@Composable
private fun ADialogPreview() {
    VTrackerTheme(darkTheme = true) {
        ADialog(true, {}) {
            Text("Dialog content")
        }
    }
}