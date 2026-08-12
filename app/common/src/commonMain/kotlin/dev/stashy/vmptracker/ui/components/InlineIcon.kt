package dev.stashy.vmptracker.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import dev.stashy.vmptracker.ui.theme.inDp

@Composable
fun InlineIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current,
    sizeMultiplier: Float = 0.8f
) {
    val iconSize = LocalTextStyle.current.lineHeight.inDp() * sizeMultiplier

    Icon(imageVector, contentDescription, modifier.size(iconSize), tint)
}
