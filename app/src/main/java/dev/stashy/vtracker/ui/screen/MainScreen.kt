package dev.stashy.vtracker.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.stashy.vtracker.ui.component.LocalNavController
import dev.stashy.vtracker.ui.theme.VTrackerTheme
import dev.stashy.vtracker.ui.vm.MainViewmodel
import org.koin.compose.koinInject

@Composable
fun MainScreen(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewmodel: MainViewmodel = koinInject()
) {
    val navController = LocalNavController.current
    val sidePadding = 16.dp

    val trackerService by viewmodel.tracker.collectAsState()

    AnimatedContent(
        trackerService,
        label = "service loading"
    ) { tracker ->
        when (tracker) {
            null -> {
                LoadingScreen()
            }

            else -> {
                val status by tracker.status.collectAsState()

                PreviewScreen(status, contentPadding)
            }
        }
    }
}


@Preview
@Composable
private fun MainScreenPreview() {
    VTrackerTheme {
        MainScreen()
    }
}