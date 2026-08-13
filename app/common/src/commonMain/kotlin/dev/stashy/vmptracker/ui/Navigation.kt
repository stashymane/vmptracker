package dev.stashy.vmptracker.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import dev.stashy.vmptracker.model.Screen
import dev.stashy.vmptracker.model.Screens
import dev.stashy.vmptracker.ui.nav.BottomSheetSceneStrategy
import dev.stashy.vmptracker.ui.nav.MultiBackStack
import dev.stashy.vmptracker.ui.nav.navPopTransition
import dev.stashy.vmptracker.ui.nav.navTransition
import dev.stashy.vmptracker.ui.nav.predictiveTransition

typealias AppBackStack = MultiBackStack<Screen, Screen.Group>

@Composable
fun Navigation() {
    val backStack = remember { AppBackStack(Screens.Camera) }
    val peekOffset = remember { BottomSheetPeekOffset() }

    CompositionLocalProvider(
        LocalBackStack provides backStack,
        LocalBottomSheetPeekOffset provides peekOffset,
    ) {
        NavDisplay(
            backStack.backStack,
            onBack = backStack::removeLast,
            sceneStrategies = listOf(BottomSheetSceneStrategy()),
            transitionSpec = AnimatedContentTransitionScope<Scene<Screen>>::navTransition,
            popTransitionSpec = AnimatedContentTransitionScope<Scene<Screen>>::navPopTransition,
            predictivePopTransitionSpec = AnimatedContentTransitionScope<Scene<Screen>>::predictiveTransition,
            entryProvider = Screen::provideEntry,
        )
    }
}
