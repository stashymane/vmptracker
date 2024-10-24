package dev.stashy.vtracker

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.stashy.vtracker.model.dataStores
import dev.stashy.vtracker.model.settings.ConnectionSettings
import dev.stashy.vtracker.model.settings.FaceTrackerSettings
import dev.stashy.vtracker.model.settings.GeneralSettings
import dev.stashy.vtracker.model.settings.HandTrackerSettings
import dev.stashy.vtracker.service.AppService
import dev.stashy.vtracker.service.TrackerServiceImpl
import dev.stashy.vtracker.service.setupNotificationChannel
import dev.stashy.vtracker.ui.Layout
import dev.stashy.vtracker.ui.PermissionGate
import dev.stashy.vtracker.ui.screen.LoadingScreen
import dev.stashy.vtracker.ui.theme.VTrackerTheme
import dev.stashy.vtracker.ui.vm.MainViewmodel
import dev.stashy.vtracker.ui.vm.SettingsViewmodel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    val serviceState: MutableStateFlow<AppService?> = MutableStateFlow(null)

    val module = module {
        includes(dataStores)
        viewModel {
            MainViewmodel(
                serviceState.value!!,
                get(named<GeneralSettings>())
            )
        }
        viewModel {
            SettingsViewmodel(
                get(named<ConnectionSettings>()),
                get(named<FaceTrackerSettings>()),
                get(named<HandTrackerSettings>())
            )
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, TrackerServiceImpl::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        stopKoin()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupNotificationChannel()
        enableEdgeToEdge()

        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(module)
        }

        setContent {
            val trackerService by serviceState.collectAsState()

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

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TrackerServiceImpl.LocalBinder
            serviceState.tryEmit(binder.getService())
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            serviceState.tryEmit(null)
        }
    }
}
