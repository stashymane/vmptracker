package dev.stashy.vtracker.service

import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import dev.stashy.vtracker.model.settings.FaceTrackerSettings
import dev.stashy.vtracker.model.settings.HandTrackerSettings
import dev.stashy.vtracker.model.settings.PoseTrackerSettings
import dev.stashy.vtracker.model.tracking.TrackerFrame
import dev.stashy.vtracker.service.camera.CameraXService
import dev.stashy.vtracker.service.camera.analysisUseCase
import dev.stashy.vtracker.service.tracking.startTrackers
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named

class MainService() : LifecycleService(), KoinComponent, TrackerService {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    val cameraXService = CameraXService(this as LifecycleOwner)
    var trackingJob: Job? = null

    val images = Channel<Bitmap>()
    override val results: Channel<Result<TrackerFrame>> = Channel()
    override val status: MutableStateFlow<TrackerService.Status> =
        MutableStateFlow(TrackerService.Status.NotRunning)

    val resultWatcher = lifecycleScope.launch { //TODO temporary for testing
        logger.info { "WATCHING FOR RESULTS" }
        results.consumeAsFlow().collect {
            val processTime = it.getOrNull()?.processingTime ?: "N/A"
            logger.info { "Frame processing took ${processTime}ms" }
        }
        logger.info { "STOPPED WATCHING" }
    }

    override fun startTracking() {
        lifecycleScope.launch {
            status.emit(TrackerService.Status.Starting)
            setupForegroundService()
            trackingJob = launch {
                startTrackers(
                    this@MainService,
                    images,
                    get(named<FaceTrackerSettings>()),
                    get(named<HandTrackerSettings>()),
                    get(named<PoseTrackerSettings>())
                ).consumeAsFlow()
                    .onStart { status.emit(TrackerService.Status.Running) }
                    .onCompletion { cause ->
                        if (cause == null || cause is CancellationException)
                            status.emit(TrackerService.Status.NotRunning)
                        else {
                            status.emit(TrackerService.Status.Error(cause))
                            logger.error(cause) { "Tracking job has encountered an exception." }
                        }
                    }
                    .collect(results::send)
            }
            analysisUseCase.setAnalyzer(Runnable::run) { image ->
                images.trySend(image.toBitmap())
                image.close()
            }
            cameraXService.start(analysisUseCase)
        }
    }

    override fun stopTracking() {
        cameraXService.stop(analysisUseCase)
        analysisUseCase.clearAnalyzer()
        trackingJob?.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    internal fun setupForegroundService() {
        try {
            ServiceCompat.startForeground(
                this,
                100,
                serviceNotification(),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
                else
                    0
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        stopTracking()
        super.onDestroy()
    }

    /**
     * Starts the service from a received intent, and sets this up as a foreground service.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        setupForegroundService()
        return START_STICKY
    }

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): MainService = this@MainService
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return binder
    }
}