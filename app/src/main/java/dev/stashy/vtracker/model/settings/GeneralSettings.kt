package dev.stashy.vtracker.model.settings

import kotlinx.serialization.Serializable

@Serializable
data class GeneralSettings(
    val cameraId: String
)
