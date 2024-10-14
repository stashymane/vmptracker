package dev.stashy.vtracker.ui.vm

import androidx.lifecycle.ViewModel
import dev.stashy.vtracker.service.TrackerService
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewmodel(
    val tracker: MutableStateFlow<TrackerService?>
) : ViewModel() {
    val fps = MutableStateFlow(0)

    fun start() {
        tracker.value?.start()
    }

    fun stop() {
        tracker.value?.stop()
    }

    fun toggle() {
        if (tracker.value?.status?.value is TrackerService.Status.Running)
            stop()
        else
            start()
    }
}