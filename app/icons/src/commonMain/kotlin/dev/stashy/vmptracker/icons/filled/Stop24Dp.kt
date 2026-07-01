package dev.stashy.vmptracker.icons.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.icons.Icons

val Icons.Filled.Stop24Dp: ImageVector
    get() {
        if (_Stop24Dp != null) {
            return _Stop24Dp!!
        }
        _Stop24Dp = ImageVector.Builder(
            name = "Filled.Stop24Dp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE3E3E3))) {
                moveTo(240f, 720f)
                verticalLineToRelative(-480f)
                horizontalLineToRelative(480f)
                verticalLineToRelative(480f)
                lineTo(240f, 720f)
                close()
            }
        }.build()

        return _Stop24Dp!!
    }

@Suppress("ObjectPropertyName")
private var _Stop24Dp: ImageVector? = null
