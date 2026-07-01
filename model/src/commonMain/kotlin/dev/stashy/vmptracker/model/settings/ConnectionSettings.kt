package dev.stashy.vmptracker.model.settings

import dev.stashy.vmptracker.model.IpAddress
import kotlinx.serialization.Serializable

@Serializable
data class ConnectionSettings(
    val address: IpAddress = IpAddress("127.0.0.1", 5123)
)
