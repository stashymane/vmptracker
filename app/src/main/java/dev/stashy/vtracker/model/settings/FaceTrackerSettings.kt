package dev.stashy.vtracker.model.settings

import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import kotlinx.serialization.Serializable

@Serializable
data class FaceTrackerSettings(
    val enabled: Boolean = true,
    val faces: Int = 1,
    val runner: Delegate = Delegate.GPU,
    val detectionConfidence: Float = 0.5f,
    val trackingConfidence: Float = 0.5f,
    val presenceConfidence: Float = 0.5f
)

fun FaceLandmarker.FaceLandmarkerOptions.Builder.applySettings(settings: FaceTrackerSettings): FaceLandmarker.FaceLandmarkerOptions.Builder =
    setNumFaces(settings.faces)
        .setMinFaceDetectionConfidence(settings.detectionConfidence)
        .setMinTrackingConfidence(settings.trackingConfidence)
        .setMinFacePresenceConfidence(settings.presenceConfidence)

