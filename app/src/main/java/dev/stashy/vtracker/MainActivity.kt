package dev.stashy.vtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.stashy.vtracker.ui.Layout
import dev.stashy.vtracker.ui.PermissionGate
import dev.stashy.vtracker.ui.screen.LoadingScreen
import dev.stashy.vtracker.ui.theme.VTrackerTheme
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val app = applicationContext as MainApplication

        setContent {
            val trackerService by app.serviceState.collectAsState()

            KoinAndroidContext {
                VTrackerTheme {
                    Surface {
                        AnimatedContent(trackerService, label = "Loading service screen") {
                            when (it) {
                                null -> LoadingScreen()
                                else -> PermissionGate { Layout() }
                            }
                        }
                    }
                }
            }
        }
    }
}
