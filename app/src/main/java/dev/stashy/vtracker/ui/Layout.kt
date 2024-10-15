package dev.stashy.vtracker.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.stashy.vtracker.ui.component.LocalNavController
import dev.stashy.vtracker.ui.screen.PreviewScreen
import dev.stashy.vtracker.ui.screen.Screen
import dev.stashy.vtracker.ui.screen.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Layout() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        Scaffold(
            topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text(stringResource(dev.stashy.vtracker.R.string.app_name)) },
//                actions = { }
//            )
            }
        ) { innerPadding ->
            NavHost(navController,
                Screen.Preview,
                enterTransition = { fadeIn() + scaleIn(initialScale = 1.1f) },
                exitTransition = { fadeOut() + scaleOut(targetScale = 0.9f) }) {
                composable<Screen.Preview> {
                    PreviewScreen(innerPadding)
                }
                composable<Screen.Settings> {
                    SettingsScreen(innerPadding)
                }
            }
        }
    }
}