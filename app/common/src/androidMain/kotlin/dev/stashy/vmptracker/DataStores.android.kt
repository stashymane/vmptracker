package dev.stashy.vmptracker

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.FileStorage
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import dev.stashy.vmptracker.model.settings.AppSettings
import dev.stashy.vmptracker.model.settings.FaceTrackerSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual fun dataStores(): Module = module {
    factory(named<AppSettings>()) {
        DataStoreFactory.create(
            storage = androidContext().getStorage("general")
        )
    }
    factory(named<FaceTrackerSettings>()) {
        DataStoreFactory.create(
            storage = androidContext().getStorage("faceTracker")
        )
    }
}

fun Context.getStorage(name: String): Storage<Preferences> {
    return FileStorage(
        serializer = PreferencesFileSerializer,
        produceFile = { filesDir.resolve(name) }
    )
}
