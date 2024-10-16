package dev.stashy.vtracker.service.tracking

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import java.lang.RuntimeException

class FaceTracker {
    private var landmarker: FaceLandmarker? = null
    private val task = "face_landmarker.task"

    private val channel: Channel<Result<Frame>> = Channel()
    val results: ReceiveChannel<Result<Frame>> get() = channel

    fun start(context: Context, settings: FaceTrackerSettings) {
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

    fun stop() {
        landmarker?.close()
        landmarker = null
    }

    fun feedImage(imageProxy: ImageProxy, flip: Boolean = false) {
        val frameTime = SystemClock.uptimeMillis()

        val bitmapBuffer =
            Bitmap.createBitmap(imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888)
        imageProxy.use { bitmapBuffer.copyPixelsFromBuffer(it.planes[0].buffer) }

        val matrix = Matrix().apply {
            postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
            if (flip) postScale(-1f, 1f, imageProxy.width.toFloat(), imageProxy.height.toFloat())
        }

        val rotatedBitmap = Bitmap.createBitmap(
            bitmapBuffer, 0, 0, bitmapBuffer.width, bitmapBuffer.height, matrix, true
        )

        landmarker?.detectAsync(BitmapImageBuilder(rotatedBitmap).build(), frameTime)
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
