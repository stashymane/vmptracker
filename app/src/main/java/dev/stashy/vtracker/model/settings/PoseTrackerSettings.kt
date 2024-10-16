package dev.stashy.vtracker.model.settings

import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import kotlinx.serialization.Serializable

@Serializable
data class PoseTrackerSettings(
    val poses: Int = 1,
    val runner: Delegate = Delegate.GPU,
    val detectionConfidence: Float = 0.5f,
    val presenceConfidence: Float = 0.5f,
    val trackingConfidence: Float = 0.5f
)

fun PoseLandmarker.PoseLandmarkerOptions.Builder.applySettings(settings: PoseTrackerSettings) =
    setNumPoses(settings.poses)
        .setMinPoseDetectionConfidence(settings.detectionConfidence)
        .setMinPosePresenceConfidence(settings.presenceConfidence)
        .setMinTrackingConfidence(settings.trackingConfidence)
