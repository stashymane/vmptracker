package dev.stashy.vmptracker.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.icons.Icons

val Icons.Outlined.CameraVideo24Dp: ImageVector
    get() {
        if (_CameraVideo24Dp != null) {
            return _CameraVideo24Dp!!
        }
        _CameraVideo24Dp = ImageVector.Builder(
            name = "Outlined.CameraVideo24Dp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE3E3E3))) {
                moveTo(371f, 760f)
                horizontalLineToRelative(218f)
                lineToRelative(-20f, -80f)
                lineTo(391f, 680f)
                lineToRelative(-20f, 80f)
                close()
                moveTo(360f, 600f)
                horizontalLineToRelative(240f)
                quadToRelative(83f, 0f, 141.5f, -58.5f)
                reflectiveQuadTo(800f, 400f)
                quadToRelative(0f, -83f, -58.5f, -141.5f)
                reflectiveQuadTo(600f, 200f)
                lineTo(360f, 200f)
                quadToRelative(-83f, 0f, -141.5f, 58.5f)
                reflectiveQuadTo(160f, 400f)
                quadToRelative(0f, 83f, 58.5f, 141.5f)
                reflectiveQuadTo(360f, 600f)
                close()
                moveTo(423.5f, 456.5f)
                quadTo(400f, 433f, 400f, 400f)
                reflectiveQuadToRelative(23.5f, -56.5f)
                quadTo(447f, 320f, 480f, 320f)
                reflectiveQuadToRelative(56.5f, 23.5f)
                quadTo(560f, 367f, 560f, 400f)
                reflectiveQuadToRelative(-23.5f, 56.5f)
                quadTo(513f, 480f, 480f, 480f)
                reflectiveQuadToRelative(-56.5f, -23.5f)
                close()
                moveTo(260f, 360f)
                quadToRelative(17f, 0f, 28.5f, -11.5f)
                reflectiveQuadTo(300f, 320f)
                quadToRelative(0f, -17f, -11.5f, -28.5f)
                reflectiveQuadTo(260f, 280f)
                quadToRelative(-17f, 0f, -28.5f, 11.5f)
                reflectiveQuadTo(220f, 320f)
                quadToRelative(0f, 17f, 11.5f, 28.5f)
                reflectiveQuadTo(260f, 360f)
                close()
                moveTo(160f, 840f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(129f)
                lineToRelative(21f, -84f)
                quadToRelative(-99f, -17f, -164.5f, -94.5f)
                reflectiveQuadTo(80f, 400f)
                quadToRelative(0f, -117f, 81.5f, -198.5f)
                reflectiveQuadTo(360f, 120f)
                horizontalLineToRelative(240f)
                quadToRelative(117f, 0f, 198.5f, 81.5f)
                reflectiveQuadTo(880f, 400f)
                quadToRelative(0f, 104f, -65.5f, 181.5f)
                reflectiveQuadTo(650f, 676f)
                lineToRelative(21f, 84f)
                horizontalLineToRelative(129f)
                verticalLineToRelative(80f)
                lineTo(160f, 840f)
                close()
                moveTo(593f, 513f)
                quadToRelative(47f, -47f, 47f, -113f)
                reflectiveQuadToRelative(-47f, -113f)
                quadToRelative(-47f, -47f, -113f, -47f)
                reflectiveQuadToRelative(-113f, 47f)
                quadToRelative(-47f, 47f, -47f, 113f)
                reflectiveQuadToRelative(47f, 113f)
                quadToRelative(47f, 47f, 113f, 47f)
                reflectiveQuadToRelative(113f, -47f)
                close()
                moveTo(480f, 400f)
                close()
                moveTo(371f, 760f)
                horizontalLineToRelative(218f)
                horizontalLineToRelative(-218f)
                close()
            }
        }.build()

        return _CameraVideo24Dp!!
    }

@Suppress("ObjectPropertyName")
private var _CameraVideo24Dp: ImageVector? = null
