package dev.stashy.vmptracker.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.icons.Icons

val Icons.Outlined.BatteryAndroidFrameShield24Dp: ImageVector
    get() {
        if (_BatteryAndroidFrameShield24Dp != null) {
            return _BatteryAndroidFrameShield24Dp!!
        }
        _BatteryAndroidFrameShield24Dp = ImageVector.Builder(
            name = "Outlined.BatteryAndroidFrameShield24Dp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE3E3E3))) {
                moveTo(160f, 720f)
                quadToRelative(-50f, 0f, -85f, -35f)
                reflectiveQuadToRelative(-35f, -85f)
                verticalLineToRelative(-240f)
                quadToRelative(0f, -50f, 35f, -85f)
                reflectiveQuadToRelative(85f, -35f)
                horizontalLineToRelative(482f)
                lineToRelative(-154f, 80f)
                lineTo(160f, 320f)
                quadToRelative(-17f, 0f, -28.5f, 11.5f)
                reflectiveQuadTo(120f, 360f)
                verticalLineToRelative(240f)
                quadToRelative(0f, 17f, 11.5f, 28.5f)
                reflectiveQuadTo(160f, 640f)
                horizontalLineToRelative(405f)
                quadToRelative(18f, 25f, 38.5f, 45f)
                reflectiveQuadToRelative(42.5f, 35f)
                lineTo(160f, 720f)
                close()
                moveTo(740f, 680f)
                quadToRelative(-80f, -29f, -130f, -118f)
                reflectiveQuadToRelative(-50f, -202f)
                quadToRelative(41f, -5f, 85f, -25f)
                reflectiveQuadToRelative(95f, -55f)
                quadToRelative(51f, 35f, 95f, 55f)
                reflectiveQuadToRelative(85f, 25f)
                quadToRelative(0f, 113f, -50f, 202f)
                reflectiveQuadTo(740f, 680f)
                close()
                moveTo(740f, 591f)
                quadToRelative(35f, -23f, 60f, -68f)
                reflectiveQuadToRelative(35f, -102f)
                quadToRelative(-23f, -8f, -46.5f, -19.5f)
                reflectiveQuadTo(740f, 375f)
                verticalLineToRelative(216f)
                close()
                moveTo(540f, 600f)
                lineTo(160f, 600f)
                verticalLineToRelative(-240f)
                horizontalLineToRelative(320f)
                quadToRelative(0f, 72f, 16.5f, 132f)
                reflectiveQuadTo(540f, 600f)
                close()
            }
        }.build()

        return _BatteryAndroidFrameShield24Dp!!
    }

@Suppress("ObjectPropertyName")
private var _BatteryAndroidFrameShield24Dp: ImageVector? = null
