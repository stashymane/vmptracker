package dev.stashy.vtracker.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.stashy.vtracker.ui.theme.VTrackerTheme
import dev.stashy.vtracker.ui.vm.MainViewmodel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewmodel: MainViewmodel = koinViewModel()
) {
    val status by viewmodel.status.collectAsState()

    PreviewScreen(status, contentPadding)
}


@Preview
@Composable
private fun MainScreenPreview() {
    VTrackerTheme {
        MainScreen()
    }
}