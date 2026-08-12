package dev.stashy.vmptracker.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween

val fastBezierEasing = CubicBezierEasing(0f, .4f, .4f, 1f)
val instantBezierEasing = CubicBezierEasing(0f, .6f, .2f, 1f)

fun <T> fastBezier(durationMillis: Int = 300, delayMillis: Int = 0) =
    tween<T>(durationMillis = durationMillis, delayMillis = delayMillis, easing = fastBezierEasing)

fun <T> instantBezier(durationMillis: Int = 300, delayMillis: Int = 0) =
    tween<T>(
        durationMillis = durationMillis,
        delayMillis = delayMillis,
        easing = instantBezierEasing
    )
