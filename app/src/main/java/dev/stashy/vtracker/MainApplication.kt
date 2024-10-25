package dev.stashy.vtracker

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import dev.stashy.vtracker.model.dataStores
import dev.stashy.vtracker.model.settings.ConnectionSettings
import dev.stashy.vtracker.model.settings.FaceTrackerSettings
import dev.stashy.vtracker.model.settings.GeneralSettings
import dev.stashy.vtracker.model.settings.HandTrackerSettings
import dev.stashy.vtracker.service.AppService
import dev.stashy.vtracker.service.TrackerServiceImpl
import dev.stashy.vtracker.ui.vm.MainViewmodel
import dev.stashy.vtracker.ui.vm.SettingsViewmodel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup.onKoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
class MainApplication : Application() {
    val serviceState: MutableStateFlow<AppService?> = MutableStateFlow(null)

    val module = module {
        includes(dataStores())
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

    init {
        onKoinStartup {
            androidLogger()
            androidContext(this@MainApplication)
            modules(dataStores(), module)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Intent(this, TrackerServiceImpl::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
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
