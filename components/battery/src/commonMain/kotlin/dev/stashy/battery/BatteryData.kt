package dev.stashy.battery

interface BatteryData {
    val value: Float
    val percentage: Int

    val temperature: Int?

    val power: PowerStatus
    val state: LevelState
}

sealed class PowerStatus {
    object Charging : PowerStatus()
    object Discharging : PowerStatus()
}

sealed class LevelState {
    object Normal : LevelState()
    object Low : LevelState()
}
