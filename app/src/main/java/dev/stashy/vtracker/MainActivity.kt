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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dev.stashy.vtracker.service.TrackerService
import dev.stashy.vtracker.service.TrackerServiceImpl
import dev.stashy.vtracker.ui.Main
import dev.stashy.vtracker.ui.vm.MainViewmodel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    val tracker: MutableStateFlow<TrackerService?> = MutableStateFlow(null)

    val module = module {
        single { androidContext().dataStore }
        viewModel { MainViewmodel(tracker) }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, TrackerServiceImpl::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(module)
        }

        setContent {
            KoinAndroidContext {
                Main()
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
