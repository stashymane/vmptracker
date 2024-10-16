package dev.stashy.vtracker.ui.vm

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import com.google.mediapipe.tasks.core.Delegate
import dev.stashy.vtracker.model.IpAddress
import dev.stashy.vtracker.model.settings.ConnectionSettings
import dev.stashy.vtracker.model.settings.FaceTrackerSettings
import dev.stashy.vtracker.model.settings.HandTrackerSettings
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewmodel(val dataStore: DataStore<Preferences>) : ViewModel() {
    val connectionState = MutableStateFlow(ConnectionState())
    val faceTrackingState = MutableStateFlow(FaceTrackingState())
    val handTrackingState = MutableStateFlow(HandTrackingState())

    fun save() {
        val connection = connectionState.value.toSettings()
        val face = faceTrackingState.value.toSettings()
        val hand = handTrackingState.value.toSettings()
        //TODO actually save
    }

    fun reset() {
        connectionState.tryEmit(ConnectionState())
        faceTrackingState.tryEmit(FaceTrackingState())
    }

    class ConnectionState {
        val protocol = mutableStateOf("VTracker")
        val ipAddress = mutableStateOf(IpAddress("192.168.1.10", 5123))

        val ipAddressDialogVisible = mutableStateOf(false)

        fun toSettings() = ConnectionSettings.VTracker(ipAddress.value)
    }

    class FaceTrackingState {
        val runner = mutableStateOf<Delegate>(Delegate.GPU)
        val detectionConfidence = mutableFloatStateOf(0.5f)
        val trackingConfidence = mutableFloatStateOf(0.5f)
        val presenceConfidence = mutableFloatStateOf(0.5f)

        val runnerDialogVisible = mutableStateOf(false)

        fun toSettings() = FaceTrackerSettings(
            1,
            runner.value,
            detectionConfidence.floatValue,
            trackingConfidence.floatValue,
            presenceConfidence.floatValue
        )
    }

    class HandTrackingState {
        val runner = mutableStateOf<Delegate>(Delegate.GPU)
        val detectionConfidence = mutableFloatStateOf(0.5f)
        val trackingConfidence = mutableFloatStateOf(0.5f)
        val presenceConfidence = mutableFloatStateOf(0.5f)

        val runnerDialogVisible = mutableStateOf(false)

        fun toSettings() = HandTrackerSettings(
            2,
            runner.value,
            detectionConfidence.floatValue,
            trackingConfidence.floatValue,
            presenceConfidence.floatValue
        )
    }
}