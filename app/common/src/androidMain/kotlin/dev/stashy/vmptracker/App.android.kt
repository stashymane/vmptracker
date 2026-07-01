package dev.stashy.vmptracker

import android.os.Build
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.core.view.ViewCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.stashy.vmptracker.ui.LocalDeviceCorners
import dev.stashy.vmptracker.ui.LocalSettings
import dev.stashy.vmptracker.ui.LocalSettingsActions
import dev.stashy.vmptracker.ui.theme.LocalSystemColor
import dev.stashy.vmptracker.vm.SettingsViewmodel
import org.koin.androidx.compose.koinViewModel

@Composable
actual fun App() {
    val settingsViewmodel: SettingsViewmodel = koinViewModel()
    val settings by settingsViewmodel.settings.collectAsStateWithLifecycle()

    val view = LocalView.current
    val corners = remember(view) { getCornerRadius(view) }

    val systemColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        colorResource(android.R.color.system_accent1_500)
    } else Color.Red

    CompositionLocalProvider(
        LocalSystemColor provides systemColor,
        LocalSettings provides settings,
        LocalSettingsActions provides settingsViewmodel,
        LocalDeviceCorners provides corners
    ) {
        AppScreen()
    }
}

fun getCornerRadius(view: View): Path? {
    val insets = ViewCompat.getRootWindowInsets(view) ?: return null

    return insets.displayShape?.path?.asComposePath()
}
