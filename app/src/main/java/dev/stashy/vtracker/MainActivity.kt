package dev.stashy.vtracker

import android.content.ComponentName
import android.content.Context
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dev.stashy.vtracker.service.TrackerService
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
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    val tracker: MutableStateFlow<TrackerService?> = MutableStateFlow(null)

    val module = module {
        single { androidContext().dataStore }
        viewModel { MainViewmodel(tracker.value!!) }
        viewModel { SettingsViewmodel(get()) }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, TrackerServiceImpl::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()

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
            val trackerService by tracker.collectAsState()

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
            tracker.tryEmit(binder.getService())
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            tracker.tryEmit(null)
        }
    }
}
