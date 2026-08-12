package dev.stashy.vmptracker

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.StrictMode
import androidx.core.content.ContextCompat
import dev.stashy.vmptracker.camera.CameraServiceLauncher
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
                single<CameraServiceLauncher> {
                    CameraServiceLauncher {
                        ContextCompat.startForegroundService(
                            this@MainApplication,
                            Intent(this@MainApplication, MainService::class.java),
                        )
                    }
                }
            },
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

        bindService(Intent(this, MainService::class.java), connection, BIND_AUTO_CREATE)
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) = Unit

        override fun onServiceDisconnected(className: ComponentName) = Unit
    }
}
