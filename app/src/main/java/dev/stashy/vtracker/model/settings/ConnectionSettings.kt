package dev.stashy.vtracker.model.settings

import dev.stashy.vtracker.model.IpAddress
import kotlinx.serialization.Serializable

@Serializable
sealed class ConnectionSettings {
    @Serializable
    data class VTracker(
        val address: IpAddress = IpAddress("127.0.0.1", 5123)
    ) : ConnectionSettings()
}
