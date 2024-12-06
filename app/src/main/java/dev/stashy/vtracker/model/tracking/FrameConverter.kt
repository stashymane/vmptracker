package dev.stashy.vtracker.model.tracking

import com.google.mediapipe.tasks.components.containers.Category
import com.google.mediapipe.tasks.components.containers.Landmark
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.jvm.optionals.getOrNull

fun FaceLandmarkerResult.toData(): List<TrackingData> = buildList {
    val landmarks = faceLandmarks()
    val blendshapes = faceBlendshapes().getOrNull()

    for (i in 0..landmarks.size) {
        add(
            TrackingData.Face(
                landmarks[i].map(NormalizedLandmark::toPos),
                blendshapes?.get(i)?.toMap() ?: mapOf()
            )
        )
    }
}

fun HandLandmarkerResult.toData(): List<TrackingData> = buildList {

}

fun PoseLandmarkerResult.toData(): List<TrackingData> = buildList {
    val landmarks = landmarks()
    val worldLandmarks = worldLandmarks()

    for (i in 0..landmarks.size) {
        add(
            TrackingData.Pose(
                landmarks[i].map(NormalizedLandmark::toPos),
                worldLandmarks[i].map(Landmark::toPos)
            )
        )
    }
}

fun NormalizedLandmark.toPos(): TrackingData.Pos = TrackingData.Pos(x(), y(), z())
fun Landmark.toPos(): TrackingData.Pos = TrackingData.Pos(x(), y(), z())
fun List<Category>.toMap(): Map<String, Float> =
    associate { category -> category.categoryName() to category.score() }
