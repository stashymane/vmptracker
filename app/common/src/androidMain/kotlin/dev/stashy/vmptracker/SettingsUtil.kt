package dev.stashy.vmptracker

import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import dev.stashy.vmptracker.model.settings.FaceTrackerSettings

fun FaceLandmarker.FaceLandmarkerOptions.Builder.applySettings(settings: FaceTrackerSettings): FaceLandmarker.FaceLandmarkerOptions.Builder =
    setNumFaces(settings.faces)
        .setMinFaceDetectionConfidence(settings.detectionConfidence)
        .setMinTrackingConfidence(settings.trackingConfidence)
        .setMinFacePresenceConfidence(settings.presenceConfidence)
