package dev.stashy.vtracker.service.tracking

import android.content.Context
import android.graphics.Bitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult
import dev.stashy.vtracker.model.settings.FaceTrackerSettings
import dev.stashy.vtracker.model.settings.applySettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

private const val task = "face_landmarker.task"

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.faceTracker(
    context: Context,
    images: ReceiveChannel<Bitmap>,
    settings: FaceTrackerSettings,
): ReceiveChannel<Result<TrackerFrame<FaceLandmarkerResult>>> =
    produce {
        val landmarker = FaceLandmarker.createFromOptions(context, settings.asOptions())

        invokeOnClose { cause ->
            landmarker.close()
        }

        images.consumeAsFlow().buffer(1, BufferOverflow.DROP_OLDEST).collect { image ->
            send(runCatching {
                val timestamp = Clock.System.now()
                val result = landmarker.detectSuspending(BitmapImageBuilder(image).build())
                TrackerFrame(result, Clock.System.now() - timestamp, image.width to image.height)
            })
        }
    }

private fun FaceTrackerSettings.asOptions(): FaceLandmarker.FaceLandmarkerOptions {
    val baseOptions = BaseOptions.builder()
        .setDelegate(runner)
        .setModelAssetPath(task)
        .build()

    val landmarkerOptions = FaceLandmarker.FaceLandmarkerOptions.builder()
        .setBaseOptions(baseOptions)
        .applySettings(this)
        .setOutputFaceBlendshapes(true)
        .setRunningMode(RunningMode.LIVE_STREAM)
//        .setResultListener(onResult)
//        .setErrorListener(onError)
        .build()

    return landmarkerOptions
}

suspend fun FaceLandmarker.detectSuspending(image: MPImage): FaceLandmarkerResult =
    withContext(Dispatchers.IO) { detect(image) }
