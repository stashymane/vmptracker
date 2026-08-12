package dev.stashy.vmptracker.ui.theme

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.stashy.vmptracker.model.Screens
import dev.stashy.vmptracker.ui.AppBackStack
import dev.stashy.vmptracker.ui.LocalBackStack
import dev.stashy.vmptracker.ui.LocalSettings
import dev.stashy.vmptracker.ui.LocalSettingsActions
import dev.stashy.vmptracker.vm.SettingsViewmodel

@Composable
fun PreviewHost(
    content: @Composable () -> Unit
) {
    val backStack = AppBackStack(Screens.Camera)
    val settings = viewModel<SettingsViewmodel>()

    CompositionLocalProvider(
        LocalSettings provides settings.settings.collectAsState().value,
        LocalSettingsActions provides settings,
        LocalBackStack provides backStack
    ) {
        AppTheme() {
            Surface {
                content()
            }
        }
    }
}

@Preview(
    name = "Dark",
    group = "Component",
    backgroundColor = 0xFF000000,
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light",
    group = "Component",
    backgroundColor = 0xFFFFFFFF,
    showBackground = true
)
annotation class ComponentPreview

@Preview(
    name = "Phone Dark",
    group = "Phone",
    uiMode = UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp"
)
@Preview(
    name = "Phone Light",
    group = "Phone",
    device = "spec:width=411dp,height=891dp"
)
@Preview(
    name = "Tablet Dark",
    group = "Tablet",
    uiMode = UI_MODE_NIGHT_YES,
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
@Preview(
    name = "Tablet Light",
    group = "Tablet",
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
annotation class DevicePreview
