package dev.stashy.vtracker.ui.vm

import androidx.lifecycle.ViewModel
import dev.stashy.vtracker.model.FaceTrackerSettings
import dev.stashy.vtracker.tracking.FaceTracker.Frame
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewmodel : ViewModel() {
    var faceTrackerSettings: FaceTrackerSettings = FaceTrackerSettings()

    val fps = MutableStateFlow(0)
    val isRunning = MutableStateFlow(false)

    fun start() {
        isRunning.tryEmit(true)
    }

    fun stop() {
        isRunning.tryEmit(false)
    }

    fun receiveEvents(channel: ReceiveChannel<Result<Frame>>) {

    }
}