package dev.stashy.vmptracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle

@Composable
fun AppTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    color: Color = LocalSystemColor.current,
    style: PaletteStyle = PaletteStyle.TonalSpot,
    content: @Composable () -> Unit
) = DynamicMaterialTheme(
    color,
    isDark,
    style = style,
    content = content
)

val LocalSystemColor: ProvidableCompositionLocal<Color> = compositionLocalOf { Color.Red }
