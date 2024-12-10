package dev.stashy.vtracker.service.tracking

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import dev.stashy.vtracker.model.settings.HandTrackerSettings
import dev.stashy.vtracker.model.settings.applySettings
import dev.stashy.vtracker.model.tracking.TrackerFrame
import dev.stashy.vtracker.model.tracking.toData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

private const val task = "tasks/face_landmarker.task"

fun CoroutineScope.handTracker(
    context: Context,
    images: ReceiveChannel<Bitmap>,
    settings: HandTrackerSettings,
): ReceiveChannel<Result<TrackerFrame>> = tracker(
    images,
    create = { HandLandmarker.createFromOptions(context, settings.asOptions()) },
    close = { close() },
    process = { image ->
        detectForVideo(BitmapImageBuilder(image).build(), SystemClock.uptimeMillis()).toData()
    }
)

private fun HandTrackerSettings.asOptions(): HandLandmarker.HandLandmarkerOptions {
    val baseOptions = BaseOptions.builder()
        .setDelegate(runner)
        .setModelAssetPath(task)
        .build()

    val landmarkerOptions = HandLandmarker.HandLandmarkerOptions.builder()
        .setBaseOptions(baseOptions)
        .applySettings(this)
        .setRunningMode(RunningMode.VIDEO)
        .build()

    return landmarkerOptions
}
