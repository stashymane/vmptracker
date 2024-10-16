package dev.stashy.vtracker.model.settings

import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import kotlinx.serialization.Serializable

@Serializable
data class HandTrackerSettings(
    val enabled: Boolean = false,
    val hands: Int = 2,
    val runner: Delegate = Delegate.GPU,
    val detectionConfidence: Float = 0.5f,
    val presenceConfidence: Float = 0.5f,
    val trackingConfidence: Float = 0.5f
)

fun HandLandmarker.HandLandmarkerOptions.Builder.applySettings(settings: HandTrackerSettings): HandLandmarker.HandLandmarkerOptions.Builder =
    setNumHands(settings.hands)
        .setMinHandDetectionConfidence(settings.detectionConfidence)
        .setMinHandPresenceConfidence(settings.presenceConfidence)
        .setMinTrackingConfidence(settings.trackingConfidence)
