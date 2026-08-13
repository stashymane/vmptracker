package dev.stashy.vmptracker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import dev.stashy.vmptracker.camera.cameraModule
import dev.stashy.vmptracker.model.settings.AppSettings
import dev.stashy.vmptracker.model.settings.CameraSettings
import dev.stashy.vmptracker.model.settings.FaceTrackerSettings
import dev.stashy.vmptracker.model.settings.dataStoreSerializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

actual fun dataStores(): Module = module {
    includes(cameraModule())

    single(named<AppSettings>()) {
        androidContext().settingsDataStore("general", dataStoreSerializer(AppSettings()))
    }
    single(named<CameraSettings>()) {
        androidContext().settingsDataStore("camera", dataStoreSerializer(CameraSettings()))
    }
    single(named<FaceTrackerSettings>()) {
        androidContext().settingsDataStore("faceTracker", dataStoreSerializer(FaceTrackerSettings()))
    }
}

private fun <T> Context.settingsDataStore(
    name: String,
    serializer: Serializer<T>,
): DataStore<T> {
    val defaultValue = serializer.defaultValue
    return DataStoreFactory.create(
        serializer = serializer,
        corruptionHandler = ReplaceFileCorruptionHandler { defaultValue },
        produceFile = { settingsFile(name) },
    )
}

private fun Context.settingsFile(name: String): File =
    filesDir.resolve("datastore").resolve("$name.json")
