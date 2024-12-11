package dev.stashy.vtracker.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import dev.stashy.vtracker.model.tracking.TrackingData
import dev.stashy.vtracker.ui.theme.VTrackerTheme

// based on https://github.com/google-ai-edge/mediapipe-samples/blob/main/examples/face_landmarker/android/app/src/main/java/com/google/mediapipe/examples/facelandmarker/OverlayView.kt

@Composable
fun TrackingPreviewCanvas(entries: List<TrackingData>, scaleFactor: Float = 1f) {
    val colors = PreviewColors(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    )

    Canvas(Modifier.fillMaxSize()) {
        for (data in entries) {
            when (data) {
                is TrackingData.Face -> {
                    drawFace(data, scaleFactor, colors)
                }

                else -> {
                    // TODO
                }
            }
        }
    }
}

private data class PreviewColors(val point: Color, val line: Color)

private fun DrawScope.drawFace(
    face: TrackingData.Face,
    scaleFactor: Float = 1f,
    colors: PreviewColors,
) {
    if (face.landmarks.isEmpty()) return

    drawPoints(
        face.landmarks.map {
            Offset(
                it.x * size.width * scaleFactor,
                it.y * size.height * scaleFactor
            )
        },
        PointMode.Points,
        color = colors.point,
        strokeWidth = 8f
    )

    FaceLandmarker.FACE_LANDMARKS_CONNECTORS.forEach {
        val start = face.landmarks[it.start()]
        val end = face.landmarks[it.end()]

        drawLine(
            start = Offset(
                start.x * size.width * scaleFactor,
                start.y * size.height * scaleFactor
            ),
            end = Offset(
                end.x * size.width * scaleFactor,
                end.y * size.height * scaleFactor
            ),
            color = colors.line,
            strokeWidth = 8f
        )
    }
}

@Preview
@Composable
private fun FaceTrackingViewPreview() {
    VTrackerTheme {
        TrackingPreviewCanvas(TODO())
    }
}