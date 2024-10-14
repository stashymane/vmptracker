package dev.stashy.vtracker.ui.component

import android.util.Size
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.tooling.preview.Preview
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult
import dev.stashy.vtracker.ui.theme.VTrackerTheme

// based on https://github.com/google-ai-edge/mediapipe-samples/blob/main/examples/face_landmarker/android/app/src/main/java/com/google/mediapipe/examples/facelandmarker/OverlayView.kt

@Composable
fun FaceTrackingView(results: FaceLandmarkerResult?, imageSize: Size, scaleFactor: Float = 1f) {
    val pointColor = MaterialTheme.colorScheme.primary
    val lineColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)

    Canvas(Modifier.fillMaxSize()) {
        if (results == null || results.faceLandmarks().isEmpty()) return@Canvas

        for (landmark in results.faceLandmarks()) {
            drawPoints(
                landmark.map {
                    Offset(
                        it.x() * size.width * scaleFactor,
                        it.y() * size.height * scaleFactor
                    )
                },
                PointMode.Points,
                color = pointColor,
                strokeWidth = 8f
            )

            FaceLandmarker.FACE_LANDMARKS_CONNECTORS.forEach {
                val landmarks = results.faceLandmarks()[0]
                val start = landmarks[it.start()]
                val end = landmarks[it.end()]

                drawLine(
                    start = Offset(
                        start.x() * size.width * scaleFactor,
                        start.y() * size.height * scaleFactor
                    ),
                    end = Offset(
                        end.x() * size.width * scaleFactor,
                        end.y() * size.height * scaleFactor
                    ),
                    color = lineColor,
                    strokeWidth = 8f
                )
            }
        }
    }
}

@Preview
@Composable
private fun FaceTrackingViewPreview() {
    VTrackerTheme {
        FaceTrackingView(TODO(), Size(100, 100))
    }
}