package dev.stashy.vtracker.ui.vm

import androidx.lifecycle.ViewModel
import dev.stashy.vtracker.service.TrackerService

class MainViewmodel(
    val tracker: TrackerService
) : ViewModel() {
    val status = tracker.status

    init {

    }

    fun start() {
        tracker.start()
    }

    fun stop() {
        tracker.stop()
    }

    fun toggle() {
        if (tracker.status.value is TrackerService.Status.Running)
            stop()
        else
            start()
    }
}