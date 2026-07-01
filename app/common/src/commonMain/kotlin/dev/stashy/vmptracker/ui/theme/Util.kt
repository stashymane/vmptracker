package dev.stashy.vmptracker.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun TextUnit.inDp(): Dp = with(LocalDensity.current) {
    this@inDp.toDp()
}
