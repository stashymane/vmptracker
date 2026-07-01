package dev.stashy.battery

import android.content.Intent
import android.os.BatteryManager
import android.os.Build

internal data class AndroidBatteryData(
    override val value: Float,
    override val percentage: Int,
    override val temperature: Int?,
    override val power: PowerStatus,
    override val state: LevelState
) : BatteryData {
    companion object {
        fun fromIntent(intent: Intent): BatteryData {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val value = level / scale.toFloat()

            val temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)

            val isCharging = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                val chargingStatus = intent.getIntExtra(BatteryManager.EXTRA_CHARGING_STATUS, -1)
                chargingStatus != 1
            } else {
                val pluggedStatus = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
                pluggedStatus != 0
            }

            val levelState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val isLow = intent.getBooleanExtra(BatteryManager.EXTRA_BATTERY_LOW, false)
                if (isLow) LevelState.Low
                else LevelState.Normal
            } else {
                LevelState.Normal
            }

            return AndroidBatteryData(
                value = value,
                percentage = (value * 100).toInt(),
                temperature = temp.validOrNull(),
                power = if (isCharging) PowerStatus.Charging else PowerStatus.Discharging,
                state = levelState
            )
        }
    }
}

private fun Int.validOrNull(): Int? = this.takeIf { it != -1 }
