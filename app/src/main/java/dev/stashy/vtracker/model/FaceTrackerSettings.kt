package dev.stashy.vtracker.model

import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import kotlinx.serialization.Serializable

@Serializable
data class FaceTrackerSettings(
    val cameraId: Int = 0,
    val delegate: Delegate = Delegate.GPU,
    val faceDetectionConfidence: Float = 0.5f,
    val faceTrackingConfidence: Float = 0.5f,
    val facePresenceConfidence: Float = 0.5f,
    val faces: Int = 1
)

fun FaceLandmarker.FaceLandmarkerOptions.Builder.applySettings(settings: FaceTrackerSettings): FaceLandmarker.FaceLandmarkerOptions.Builder =
    setMinFaceDetectionConfidence(settings.faceDetectionConfidence)
        .setMinTrackingConfidence(settings.faceTrackingConfidence)
        .setMinFacePresenceConfidence(settings.facePresenceConfidence)
        .setNumFaces(settings.faces)
