package dev.stashy.vmptracker.model.settings

import kotlinx.serialization.Serializable

@Serializable
data class FaceTrackerSettings(
    val enabled: Boolean = true,
    val faces: Int = 1,
    val runner: Runner = Runner.GPU,
    val detectionConfidence: Float = 0.5f,
    val trackingConfidence: Float = 0.5f,
    val presenceConfidence: Float = 0.5f
)
