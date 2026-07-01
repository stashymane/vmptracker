package dev.stashy.vmptracker.icons.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.icons.Icons

val Icons.Filled.PlayArrow24Dp: ImageVector
    get() {
        if (_PlayArrow24Dp != null) {
            return _PlayArrow24Dp!!
        }
        _PlayArrow24Dp = ImageVector.Builder(
            name = "Filled.PlayArrow24Dp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE3E3E3))) {
                moveTo(320f, 760f)
                verticalLineToRelative(-560f)
                lineToRelative(440f, 280f)
                lineToRelative(-440f, 280f)
                close()
            }
        }.build()

        return _PlayArrow24Dp!!
    }

@Suppress("ObjectPropertyName")
private var _PlayArrow24Dp: ImageVector? = null
