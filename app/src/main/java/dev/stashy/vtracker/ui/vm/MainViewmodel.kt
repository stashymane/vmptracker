package dev.stashy.vtracker.ui.vm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewmodel : ViewModel() {
    val fps = MutableStateFlow(0)
    val isRunning = MutableStateFlow(false)

    fun start() {
        isRunning.tryEmit(true)
    }

    fun stop() {
        isRunning.tryEmit(false)
    }
}