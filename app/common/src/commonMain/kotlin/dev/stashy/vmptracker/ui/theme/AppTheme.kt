package dev.stashy.vmptracker.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle

@Composable
fun AppTheme(
    color: Color,
    isDark: Boolean,
    style: PaletteStyle = PaletteStyle.TonalSpot,
    content: @Composable () -> Unit
) =
    DynamicMaterialTheme(
        color,
        isDark,
        style = style,
        content = content
    )
