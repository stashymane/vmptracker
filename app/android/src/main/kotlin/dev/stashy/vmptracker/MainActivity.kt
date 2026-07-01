package dev.stashy.vmptracker

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import dev.stashy.vmptracker.model.settings.AppSettings
import dev.stashy.vmptracker.ui.LocalDeviceCorners
import dev.stashy.vmptracker.ui.LocalSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent(content = ::App)
    }
}

