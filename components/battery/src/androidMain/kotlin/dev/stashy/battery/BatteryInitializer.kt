package dev.stashy.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.startup.Initializer

internal class BatteryInitializer : Initializer<BatteryMonitor> {
    override fun create(context: Context): BatteryMonitor {
        val app = context.applicationContext
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        app.registerReceiver(BatteryStateReceiver, filter)
        return BatteryMonitor
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}

private object BatteryStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val state = BatteryMonitor.State.Available(AndroidBatteryData.fromIntent(intent))
        BatteryMonitor.state.tryEmit(state)
    }
}
