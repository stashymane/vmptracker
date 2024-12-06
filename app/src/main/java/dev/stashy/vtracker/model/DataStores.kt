package dev.stashy.vtracker.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import dev.stashy.vtracker.model.settings.ConnectionSettings
import dev.stashy.vtracker.model.settings.FaceTrackerSettings
import dev.stashy.vtracker.model.settings.GeneralSettings
import dev.stashy.vtracker.model.settings.HandTrackerSettings
import dev.stashy.vtracker.model.settings.dataStoreSerializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun dataStores() = module {
    factory(named<GeneralSettings>()) { androidContext().generalSettings }
    factory(named<FaceTrackerSettings>()) { androidContext().faceTrackerSettings }
    factory(named<HandTrackerSettings>()) { androidContext().handTrackerSettings }
    factory(named<ConnectionSettings>()) { androidContext().connectionSettings }
}

val Context.generalSettings: DataStore<GeneralSettings> by dataStore(
    fileName = "general",
    serializer = dataStoreSerializer(GeneralSettings())
)

val Context.faceTrackerSettings: DataStore<FaceTrackerSettings> by dataStore(
    fileName = "faceTracker",
    serializer = dataStoreSerializer(FaceTrackerSettings())
)

val Context.handTrackerSettings: DataStore<HandTrackerSettings> by dataStore(
    fileName = "handTracker",
    serializer = dataStoreSerializer(HandTrackerSettings())
)

val Context.connectionSettings: DataStore<ConnectionSettings> by dataStore(
    fileName = "connection",
    serializer = dataStoreSerializer(ConnectionSettings())
)
