package dev.stashy.vtracker.model.tracking

import com.google.mediapipe.tasks.components.containers.Category
import com.google.mediapipe.tasks.components.containers.Landmark
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.jvm.optionals.getOrNull

fun FaceLandmarkerResult.toData(): List<TrackingData> {
    val landmarks = faceLandmarks()
    val blendshapes = faceBlendshapes().getOrNull() ?: listOf()

    return landmarks.zip(blendshapes)
        .map { (landmark, blendshape) ->
            TrackingData.Face(
                landmark.map(NormalizedLandmark::toPos),
                blendshape.toMap()
            )
        }
}

fun HandLandmarkerResult.toData(): List<TrackingData> = buildList {

}

fun PoseLandmarkerResult.toData(): List<TrackingData> {
    val landmarks = landmarks()
    val worldLandmarks = worldLandmarks()

    return landmarks.zip(worldLandmarks)
        .map { (landmark, worldLandmark) ->
            TrackingData.Pose(
                landmark.map(NormalizedLandmark::toPos),
                worldLandmark.map(Landmark::toPos)
            )
        }
}

fun NormalizedLandmark.toPos(): TrackingData.Pos = TrackingData.Pos(x(), y(), z())
fun Landmark.toPos(): TrackingData.Pos = TrackingData.Pos(x(), y(), z())
fun List<Category>.toMap(): Map<String, Float> =
    associate { category -> category.categoryName() to category.score() }
