package dev.stashy.vmptracker

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.StrictMode
import androidx.camera.lifecycle.ProcessCameraProvider
import dev.stashy.vmptracker.vm.viewmodelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
class MainApplication : Application(), KoinStartup {
    override fun onKoinStartup(): KoinConfiguration = koinConfiguration {
        androidLogger()
        androidContext(this@MainApplication)

        modules(
            dataStores(),
            viewmodelModule(),
            module {
                includes(dataStores())
                single { ProcessCameraProvider.getInstance(this@MainApplication).get() }
            }
        )
    }

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
//                    .penaltyDeath()
                    .build()
            )
        }

        super.onCreate()

        Intent(this, MainService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MainService.LocalBinder
            binder.service
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
        }
    }
}
