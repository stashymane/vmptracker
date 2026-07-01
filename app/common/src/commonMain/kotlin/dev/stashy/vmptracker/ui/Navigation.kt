package dev.stashy.vmptracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation3.ui.NavDisplay
import dev.stashy.vmptracker.ui.nav.MultiBackStack
import dev.stashy.vmptracker.ui.nav.Screen
import dev.stashy.vmptracker.ui.nav.Screens

typealias AppBackStack = MultiBackStack<Screen, Screen.Group>

@Composable
fun Navigation() {
    val backStack = remember { AppBackStack(Screens.Camera) }

    CompositionLocalProvider(LocalBackStack provides backStack) {
        NavDisplay(
            backStack.backStack,
            onBack = backStack::removeLast,
            entryProvider = Screen::provideEntry
        )
    }
}
