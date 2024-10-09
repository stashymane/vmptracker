package dev.stashy.vtracker.ui.component

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController

val LocalNavController: ProvidableCompositionLocal<NavController> =
    staticCompositionLocalOf { throw NullPointerException("LocalNavController not yet initialized.") }