package dev.stashy.vtracker.model.tracking

import kotlinx.serialization.Serializable

@Serializable
sealed class TrackingData {
    /**
     * Represents a single face.
     * [Landmark mapping](https://ai.google.dev/edge/mediapipe/solutions/vision/face_landmarker#models)
     * @property landmarks a list of 478 normalized landmarks.
     * @property blendshapes a list of 52 blendshapes.
     */
    @Serializable
    data class Face(
        val landmarks: List<Pos> = listOf(),
        val blendshapes: Map<String, Float> = mapOf(),
        // also transformation matrices, but we don't need that right now
    ) : TrackingData()

    /**
     * Represents a single pose.
     * [Landmark mapping](https://ai.google.dev/edge/mediapipe/solutions/vision/pose_landmarker#pose_landmarker_model)
     * @property normalizedLandmarks a list of 33 landmarks normalized to image size.
     * @property worldspaceLandmarks a list of 33 landmarks roughly accurate to world-space.
     */
    @Serializable
    data class Pose(
        val normalizedLandmarks: List<Pos>,
        val worldspaceLandmarks: List<Pos>,
        // + visibility + presence, not needed yet but present per-landmark
    ) : TrackingData()

    /**
     * Represents a single hand.
     * [Landmark mapping](https://ai.google.dev/edge/mediapipe/solutions/vision/hand_landmarker#models)
     * @property normalizedLandmarks a list of 21 landmarks normalized to image size.
     * @property worldspaceLandmarks a list of 21 landmarks roughly accurate to world-space.
     */
    @Serializable
    data class Hand(
        val meta: Meta,
        val normalizedLandmarks: List<Pos> = listOf(),
        val worldspaceLandmarks: List<Pos> = listOf(),
    ) : TrackingData() {
        @Serializable
        data class Meta(val index: Int, val score: Float, val category: Category)

        @Serializable
        enum class Category {
            Left, Right
        }
    }

    @Serializable
    data class Pos(val x: Float, val y: Float, val z: Float)
}