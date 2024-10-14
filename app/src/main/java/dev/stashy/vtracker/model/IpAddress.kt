package dev.stashy.vtracker.model

import kotlinx.serialization.Serializable

@Serializable
data class IpAddress(val address: String, val port: Int) {
    companion object {
        val regex =
            Regex("\\b(?:(?:2(?:[0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9])\\.){3}(?:2([0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9])\\b")

        fun validateIp(address: String) = regex.matches(address)
    }

    fun validate(): Boolean = validateIp(address)

    override fun toString(): String = "$address:$port"
}
