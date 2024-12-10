package dev.stashy.vtracker.service.tracking

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import dev.stashy.vtracker.model.settings.FaceTrackerSettings
import dev.stashy.vtracker.model.settings.applySettings
import dev.stashy.vtracker.model.tracking.TrackerFrame
import dev.stashy.vtracker.model.tracking.toData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

private const val task = "tasks/face_landmarker.task"

fun CoroutineScope.faceTracker(
    context: Context,
    images: ReceiveChannel<Bitmap>,
    settings: FaceTrackerSettings,
): ReceiveChannel<Result<TrackerFrame>> = tracker(
    images,
    create = { FaceLandmarker.createFromOptions(context, settings.asOptions()) },
    close = { close() },
    process = { image ->
        detectForVideo(BitmapImageBuilder(image).build(), SystemClock.uptimeMillis()).toData()
    }
)

private fun FaceTrackerSettings.asOptions(): FaceLandmarker.FaceLandmarkerOptions {
    val baseOptions = BaseOptions.builder()
        .setDelegate(runner)
        .setModelAssetPath(task)
        .build()

    val landmarkerOptions = FaceLandmarker.FaceLandmarkerOptions.builder()
        .setBaseOptions(baseOptions)
        .applySettings(this)
        .setOutputFaceBlendshapes(true)
        .setRunningMode(RunningMode.VIDEO)
        .build()

    return landmarkerOptions
}
