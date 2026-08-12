package dev.stashy.vmptracker.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.icons.Icons

val Icons.Outlined.MobileCamera24Dp: ImageVector
    get() {
        if (_MobileCamera24Dp != null) {
            return _MobileCamera24Dp!!
        }
        _MobileCamera24Dp = ImageVector.Builder(
            name = "Outlined.MobileCamera24Dp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE3E3E3))) {
                moveTo(280f, 720f)
                verticalLineToRelative(120f)
                verticalLineToRelative(-720f)
                verticalLineToRelative(600f)
                close()
                moveTo(508.5f, 228.5f)
                quadTo(520f, 217f, 520f, 200f)
                reflectiveQuadToRelative(-11.5f, -28.5f)
                quadTo(497f, 160f, 480f, 160f)
                reflectiveQuadToRelative(-28.5f, 11.5f)
                quadTo(440f, 183f, 440f, 200f)
                reflectiveQuadToRelative(11.5f, 28.5f)
                quadTo(463f, 240f, 480f, 240f)
                reflectiveQuadToRelative(28.5f, -11.5f)
                close()
                moveTo(680f, 720f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(120f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(680f, 920f)
                lineTo(280f, 920f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(200f, 840f)
                verticalLineToRelative(-720f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(280f, 40f)
                horizontalLineToRelative(400f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(760f, 120f)
                verticalLineToRelative(120f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(-120f)
                lineTo(280f, 120f)
                verticalLineToRelative(720f)
                horizontalLineToRelative(400f)
                verticalLineToRelative(-120f)
                close()
                moveTo(580f, 640f)
                quadToRelative(-25f, 0f, -42.5f, -17.5f)
                reflectiveQuadTo(520f, 580f)
                verticalLineToRelative(-160f)
                quadToRelative(0f, -25f, 17.5f, -42.5f)
                reflectiveQuadTo(580f, 360f)
                horizontalLineToRelative(40f)
                lineToRelative(40f, -40f)
                horizontalLineToRelative(80f)
                lineToRelative(40f, 40f)
                horizontalLineToRelative(40f)
                quadToRelative(25f, 0f, 42.5f, 17.5f)
                reflectiveQuadTo(880f, 420f)
                verticalLineToRelative(160f)
                quadToRelative(0f, 25f, -17.5f, 42.5f)
                reflectiveQuadTo(820f, 640f)
                lineTo(580f, 640f)
                close()
                moveTo(749.5f, 549.5f)
                quadTo(770f, 529f, 770f, 500f)
                reflectiveQuadToRelative(-20.5f, -49.5f)
                quadTo(729f, 430f, 700f, 430f)
                reflectiveQuadToRelative(-49.5f, 20.5f)
                quadTo(630f, 471f, 630f, 500f)
                reflectiveQuadToRelative(20.5f, 49.5f)
                quadTo(671f, 570f, 700f, 570f)
                reflectiveQuadToRelative(49.5f, -20.5f)
                close()
            }
        }.build()

        return _MobileCamera24Dp!!
    }

@Suppress("ObjectPropertyName")
private var _MobileCamera24Dp: ImageVector? = null
