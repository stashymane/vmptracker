package dev.stashy.vtracker.ui.vm

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mediapipe.tasks.core.Delegate
import dev.stashy.vtracker.model.settings.ConnectionSettings
import dev.stashy.vtracker.model.settings.FaceTrackerSettings
import dev.stashy.vtracker.model.settings.HandTrackerSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewmodel(
    val connectionSettings: DataStore<ConnectionSettings>,
    val faceSettings: DataStore<FaceTrackerSettings>,
    val handSettings: DataStore<HandTrackerSettings>
) : ViewModel() {
    val connectionState = MutableStateFlow(ConnectionState())
    val faceTrackingState = MutableStateFlow(FaceTrackingState())
    val handTrackingState = MutableStateFlow(HandTrackingState())
    //TODO all this repetition can be abstracted away lol

    init {
        viewModelScope.launch {
            connectionState.emit(ConnectionState(connectionSettings.data.first()))
            faceTrackingState.emit(FaceTrackingState(faceSettings.data.first()))
            handTrackingState.emit(HandTrackingState(handSettings.data.first()))
        }
    }

    fun save(onFinished: () -> Unit) = viewModelScope.launch {
        connectionSettings.updateData { connectionState.value.toSettings() }
        faceSettings.updateData { faceTrackingState.value.toSettings() }
        handSettings.updateData { handTrackingState.value.toSettings() }

        onFinished()
    }

    fun reset() = viewModelScope.launch {
        connectionState.emit(ConnectionState())
        faceTrackingState.emit(FaceTrackingState())
        handTrackingState.emit(HandTrackingState())
    }

    class ConnectionState(settings: ConnectionSettings = ConnectionSettings()) {
        val protocol = mutableStateOf("VTracker")
        val ipAddress = mutableStateOf(settings.address)

        val ipAddressDialogVisible = mutableStateOf(false)

        fun toSettings(): ConnectionSettings = ConnectionSettings(ipAddress.value)
    }

    class FaceTrackingState(settings: FaceTrackerSettings = FaceTrackerSettings()) {
        val enabled = mutableStateOf(settings.enabled)
        val runner = mutableStateOf<Delegate>(settings.runner)
        val detectionConfidence = mutableFloatStateOf(settings.detectionConfidence)
        val trackingConfidence = mutableFloatStateOf(settings.trackingConfidence)
        val presenceConfidence = mutableFloatStateOf(settings.presenceConfidence)

        val runnerDialogVisible = mutableStateOf(false)

        fun toSettings(): FaceTrackerSettings = FaceTrackerSettings(
            enabled.value,
            1,
            runner.value,
            detectionConfidence.floatValue,
            trackingConfidence.floatValue,
            presenceConfidence.floatValue
        )
    }

    class HandTrackingState(settings: HandTrackerSettings = HandTrackerSettings()) {
        val enabled = mutableStateOf(settings.enabled)
        val runner = mutableStateOf<Delegate>(settings.runner)
        val detectionConfidence = mutableFloatStateOf(settings.detectionConfidence)
        val trackingConfidence = mutableFloatStateOf(settings.trackingConfidence)
        val presenceConfidence = mutableFloatStateOf(settings.presenceConfidence)

        val runnerDialogVisible = mutableStateOf(false)

        fun toSettings(): HandTrackerSettings = HandTrackerSettings(
            enabled.value,
            2,
            runner.value,
            detectionConfidence.floatValue,
            trackingConfidence.floatValue,
            presenceConfidence.floatValue
        )
    }
}