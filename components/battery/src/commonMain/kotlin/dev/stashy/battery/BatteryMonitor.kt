package dev.stashy.battery

import kotlinx.coroutines.flow.MutableStateFlow

object BatteryMonitor {
    val state: MutableStateFlow<State> =
        MutableStateFlow(State.Unavailable)

    sealed class State {
        object Unavailable : State()
        class Available(val data: BatteryData) : State()
    }
}
