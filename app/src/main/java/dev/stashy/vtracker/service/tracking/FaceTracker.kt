package dev.stashy.vtracker.service.tracking

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult
import dev.stashy.vtracker.model.settings.FaceTrackerSettings
import dev.stashy.vtracker.model.settings.applySettings
import dev.stashy.vtracker.service.tracking.FaceTracker.Frame
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import java.lang.RuntimeException

class FaceTracker : Tracker<Result<Frame>, FaceTrackerSettings> {
    private var landmarker: FaceLandmarker? = null
    private val task = "face_landmarker.task"

    private val channel: Channel<Result<Frame>> = Channel()
    override val results: ReceiveChannel<Result<Frame>> get() = channel

    override fun start(context: Context, settings: FaceTrackerSettings) {
        landmarker?.close()

        val baseOptions = BaseOptions.builder()
            .setDelegate(settings.runner)
            .setModelAssetPath(task)
            .build()

        val landmarkerOptions = FaceLandmarker.FaceLandmarkerOptions.builder()
            .setBaseOptions(baseOptions)
            .applySettings(settings)
            .setOutputFaceBlendshapes(true)
            .setRunningMode(RunningMode.LIVE_STREAM)
            .setResultListener(::receiveResult)
            .setErrorListener(::receiveError)
            .build()

        try {
            landmarker = FaceLandmarker.createFromOptions(context, landmarkerOptions)
        } catch (e: Exception) {
            channel.trySend(
                Result.failure(RuntimeException("Failed to initialize face landmarker.", e))
            )
        }
    }

    override fun stop() {
        landmarker?.close()
        landmarker = null
    }

    fun asd(imageProxy: ImageProxy, flip: Boolean = false) {
        //TODO move this out of facetracker and do for all trackers

    }

    override fun submit(image: Bitmap) {
        val frameTime = SystemClock.uptimeMillis()
        landmarker?.detectAsync(BitmapImageBuilder(image).build(), frameTime)
    }

    private fun receiveResult(result: FaceLandmarkerResult, image: MPImage) {
        channel.trySend(
            Result.success(
                Frame(
                    result,
                    SystemClock.uptimeMillis() - result.timestampMs(),
                    image.width,
                    image.height
                )
            )
        )
    }

    private fun receiveError(error: Exception) {
        channel.trySend(Result.failure(error))
    }

    data class Frame(
        val result: FaceLandmarkerResult,
        val delta: Long,
        val width: Int,
        val height: Int
    )
}
