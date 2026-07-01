package dev.stashy.vmptracker.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.icons.Icons

val Icons.Outlined.LeakAdd24Dp: ImageVector
    get() {
        if (_LeakAdd24Dp != null) {
            return _LeakAdd24Dp!!
        }
        _LeakAdd24Dp = ImageVector.Builder(
            name = "Outlined.LeakAdd24Dp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE3E3E3))) {
                moveTo(120f, 560f)
                verticalLineToRelative(-80f)
                quadToRelative(74f, 0f, 139.5f, -28.5f)
                reflectiveQuadTo(374f, 374f)
                quadToRelative(49f, -49f, 77.5f, -114.5f)
                reflectiveQuadTo(480f, 120f)
                horizontalLineToRelative(80f)
                quadToRelative(0f, 91f, -34.5f, 171f)
                reflectiveQuadTo(431f, 431f)
                quadToRelative(-60f, 60f, -140f, 94.5f)
                reflectiveQuadTo(120f, 560f)
                close()
                moveTo(120f, 400f)
                verticalLineToRelative(-80f)
                quadToRelative(83f, 0f, 141.5f, -58.5f)
                reflectiveQuadTo(320f, 120f)
                horizontalLineToRelative(80f)
                quadToRelative(0f, 116f, -82f, 198f)
                reflectiveQuadToRelative(-198f, 82f)
                close()
                moveTo(120f, 240f)
                verticalLineToRelative(-120f)
                horizontalLineToRelative(120f)
                quadToRelative(0f, 50f, -35f, 85f)
                reflectiveQuadToRelative(-85f, 35f)
                close()
                moveTo(400f, 840f)
                quadToRelative(0f, -91f, 34.5f, -171f)
                reflectiveQuadTo(529f, 529f)
                quadToRelative(60f, -60f, 140f, -94.5f)
                reflectiveQuadTo(840f, 400f)
                verticalLineToRelative(80f)
                quadToRelative(-74f, 0f, -139.5f, 28.5f)
                reflectiveQuadTo(586f, 586f)
                quadToRelative(-49f, 49f, -77.5f, 114.5f)
                reflectiveQuadTo(480f, 840f)
                horizontalLineToRelative(-80f)
                close()
                moveTo(560f, 840f)
                quadToRelative(0f, -116f, 82f, -198f)
                reflectiveQuadToRelative(198f, -82f)
                verticalLineToRelative(80f)
                quadToRelative(-83f, 0f, -141.5f, 58.5f)
                reflectiveQuadTo(640f, 840f)
                horizontalLineToRelative(-80f)
                close()
                moveTo(720f, 840f)
                quadToRelative(0f, -50f, 35f, -85f)
                reflectiveQuadToRelative(85f, -35f)
                verticalLineToRelative(120f)
                lineTo(720f, 840f)
                close()
            }
        }.build()

        return _LeakAdd24Dp!!
    }

@Suppress("ObjectPropertyName")
private var _LeakAdd24Dp: ImageVector? = null
