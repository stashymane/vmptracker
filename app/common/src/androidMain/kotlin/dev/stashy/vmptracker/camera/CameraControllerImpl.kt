package dev.stashy.vmptracker.camera

import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.Camera
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalLensFacing
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.abs

/**
 * Owns CameraX preview binding for the viewfinder and foreground-service promotion while
 * tracking is active. Video capture is not wired yet.
 *
 * Back cameras bind [CameraSelector.DEFAULT_BACK_CAMERA] so vendor logical cameras
 * (e.g. Pixel) can switch physical lenses automatically via zoom ratio.
 */
class CameraControllerImpl(
    private val cameraProvider: ProcessCameraProvider,
    private val serviceLauncher: CameraServiceLauncher = NoOpCameraServiceLauncher,
) : CameraController, CameraPreviewHost {
    override val surfaceRequest: StateFlow<SurfaceRequest?>
        field = MutableStateFlow<SurfaceRequest?>(null)

    override val zoomState: StateFlow<CameraZoomState>
        field = MutableStateFlow(CameraZoomState())

    override val lensState: StateFlow<CameraLensState>
        field = MutableStateFlow(CameraLensState())

    override val captureState: StateFlow<CameraCaptureState>
        field = MutableStateFlow(CameraCaptureState())

    private var boundCamera: Camera? = null
    private var preview: Preview? = null
    private var previewLifecycleOwner: LifecycleOwner? = null
    private var previewActive: Boolean = false
    private var trackingActive: Boolean = false
    private var onTrackingStarted: (() -> Unit)? = null
    private var onTrackingStopped: (() -> Unit)? = null
    private var selectedLensId: String? = null
    private var preferredFrameRate: Int = 60
    private var preferredZoomRatio: Float? = null
    private var pendingZoomRatio: Float? = null

    init {
        val lenses = discoverLenses()
        val defaultLensId = selectDefaultLens(lenses)
        selectedLensId = defaultLensId
        lensState.value = CameraLensState(lenses = lenses, selectedId = defaultLensId)

        val defaultLens = lenses.find { it.id == defaultLensId }
        val cameraInfo = cameraInfoFor(cameraSelectorFor(defaultLens), defaultLensId ?: "")
        if (cameraInfo != null) {
            captureState.value = CameraCaptureState(
                frameRate = preferredFrameRate.coerceAtMost(CameraSessionFactory.VIEWFINDER_MAX_FPS),
                supportedFrameRates = CameraSessionFactory.supportedFrameRates(cameraInfo),
            )
        }
    }

    fun attachService(
        onStarted: () -> Unit = {},
        onStopped: () -> Unit = {},
    ) {
        onTrackingStarted = onStarted
        onTrackingStopped = onStopped
    }

    fun detachService() {
        stopTracking()
        onTrackingStarted = null
        onTrackingStopped = null
    }

    fun startPreview(lifecycleOwner: LifecycleOwner) {
        previewLifecycleOwner = lifecycleOwner
        previewActive = true
        performBind(lifecycleOwner)
    }

    fun stopPreview() {
        previewActive = false
        previewLifecycleOwner = null
        releaseCamera()
    }

    override fun startTracking() {
        if (trackingActive) return

        trackingActive = true
        serviceLauncher.ensureStarted()
        onTrackingStarted?.invoke()
    }

    override fun stopTracking() {
        if (!trackingActive) return

        trackingActive = false
        onTrackingStopped?.invoke()
    }

    override fun setPreferredFrameRate(fps: Int) {
        val clamped = fps.coerceIn(1, 240)
        if (preferredFrameRate == clamped) return
        preferredFrameRate = clamped

        val owner = previewLifecycleOwner
        if (previewActive && owner != null) {
            performBind(owner)
        }
    }

    override fun setPreferredZoomRatio(ratio: Float?) {
        preferredZoomRatio = ratio
        ratio?.let { setZoomRatio(it) }
    }

    override fun selectLens(lensId: String) {
        if (lensState.value.lenses.none { it.id == lensId }) return
        if (selectedLensId == lensId) return

        selectedLensId = lensId
        lensState.update { it.copy(selectedId = lensId) }

        val lens = lensState.value.lenses.find { it.id == lensId }

        pendingZoomRatio = if (lens?.facing == LensFacing.Back) lens.intrinsicZoomRatio else null

        val owner = previewLifecycleOwner
        if (previewActive && owner != null) {
            performBind(owner)
        }
    }

    override fun setZoomRatio(ratio: Float) {
        val camera = boundCamera ?: return
        val zoom = camera.cameraInfo.zoomState.value ?: return
        val clamped = ratio.coerceIn(zoom.minZoomRatio, zoom.maxZoomRatio)
        camera.cameraControl.setZoomRatio(clamped)
    }

    private fun performBind(lifecycleOwner: LifecycleOwner) {
        val lensId = selectedLensId ?: return

        releaseCamera()

        val lens = lensState.value.lenses.find { it.id == lensId }
        val cameraSelector = cameraSelectorFor(lens)
        val cameraInfo = cameraInfoFor(cameraSelector, lensId) ?: return

        val sessionSetup = CameraSessionFactory.createPreview(
            cameraInfo = cameraInfo,
            onSurfaceRequest = { request -> surfaceRequest.value = request },
            preferredFps = preferredFrameRate,
        )

        preview = sessionSetup.preview
        captureState.value = sessionSetup.captureState

        try {
            cameraProvider.unbindAll()
            val camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                sessionSetup.preview,
            )
            boundCamera = camera

            val pendingZoom = pendingZoomRatio ?: preferredZoomRatio
            pendingZoomRatio = null
            if (pendingZoom != null) {
                setZoomRatio(pendingZoom)
            }

            camera.cameraInfo.zoomState.observe(lifecycleOwner) { zoom ->
                val stops = buildZoomStops(
                    minZoomRatio = zoom.minZoomRatio,
                    maxZoomRatio = zoom.maxZoomRatio,
                )
                zoomState.value = CameraZoomState(
                    zoomRatio = zoom.zoomRatio,
                    minZoomRatio = zoom.minZoomRatio,
                    maxZoomRatio = zoom.maxZoomRatio,
                    stops = stops,
                )
            }
        } catch (_: Exception) {
            preview = null
            boundCamera = null
            surfaceRequest.value = null
            captureState.value = captureState.value.copy(
                frameRate = preferredFrameRate.coerceAtMost(CameraSessionFactory.VIEWFINDER_MAX_FPS),
            )
        }
    }

    private fun releaseCamera() {
        val lifecycleOwner = previewLifecycleOwner
        if (lifecycleOwner != null && boundCamera != null) {
            boundCamera?.cameraInfo?.zoomState?.removeObservers(lifecycleOwner)
        }
        if (boundCamera != null) {
            cameraProvider.unbindAll()
        }
        boundCamera = null
        preview = null
        surfaceRequest.value = null
        captureState.value = captureState.value.copy(
            frameRate = preferredFrameRate.coerceAtMost(CameraSessionFactory.VIEWFINDER_MAX_FPS),
        )
    }

    private fun cameraSelectorFor(lens: CameraLens?): CameraSelector = when (lens?.facing) {
        LensFacing.Back -> CameraSelector.DEFAULT_BACK_CAMERA
        LensFacing.Front -> CameraSelector.DEFAULT_FRONT_CAMERA
        else -> cameraSelectorForId(lens?.id)
    }

    @OptIn(ExperimentalCamera2Interop::class)
    private fun cameraSelectorForId(lensId: String?): CameraSelector {
        if (lensId == null) return CameraSelector.DEFAULT_BACK_CAMERA
        return CameraSelector.Builder()
            .addCameraFilter { cameraInfos ->
                cameraInfos.filter { cameraInfo ->
                    Camera2CameraInfo.from(cameraInfo).cameraId == lensId
                }
            }
            .build()
    }

    @OptIn(ExperimentalCamera2Interop::class)
    private fun cameraInfoFor(cameraSelector: CameraSelector, lensId: String): CameraInfo? =
        runCatching { cameraSelector.filter(cameraProvider.availableCameraInfos).firstOrNull() }
            .getOrNull()
            ?: cameraProvider.availableCameraInfos.find { info ->
                Camera2CameraInfo.from(info).cameraId == lensId
            }

    private fun discoverLenses(): List<CameraLens> =
        cameraProvider.availableCameraInfos
            .mapNotNull { cameraInfo -> cameraInfo.toCameraLens() }
            .sortedWith(
                compareBy<CameraLens> { lensFacingSortKey(it.facing) }
                    .thenBy { it.intrinsicZoomRatio },
            )

    private fun selectDefaultLens(lenses: List<CameraLens>): String? {
        val backLenses = lenses.filter { it.facing == LensFacing.Back }
        val defaultBack = backLenses.minByOrNull { abs(it.intrinsicZoomRatio - 1f) }
        return defaultBack?.id ?: lenses.firstOrNull()?.id
    }

    private fun buildZoomStops(
        minZoomRatio: Float,
        maxZoomRatio: Float,
    ): List<Float> {
        return buildList {
            if (minZoomRatio < 1f) add(minZoomRatio)
            add(1f)
            add(2f)
            add(5f)
        }.filter { zoom ->
            zoom in minZoomRatio..maxZoomRatio
        }
    }

    @OptIn(ExperimentalCamera2Interop::class)
    private fun CameraInfo.toCameraLens(): CameraLens? {
        val cameraId =
            runCatching { Camera2CameraInfo.from(this).cameraId }.getOrNull() ?: return null
        val capabilities = CameraLensCapabilities.probe(this)
        return CameraLens(
            id = cameraId,
            facing = toLensFacing(),
            intrinsicZoomRatio = intrinsicZoomRatio.takeIf { it > 0f } ?: 1f,
            maxVideoWidth = capabilities.maxVideoWidth,
            maxVideoHeight = capabilities.maxVideoHeight,
            supportedFrameRates = capabilities.supportedFrameRates,
        )
    }

    @OptIn(ExperimentalLensFacing::class)
    private fun CameraInfo.toLensFacing(): LensFacing = when (lensFacing) {
        CameraSelector.LENS_FACING_FRONT -> LensFacing.Front
        CameraSelector.LENS_FACING_BACK -> LensFacing.Back
        CameraSelector.LENS_FACING_EXTERNAL -> LensFacing.External
        else -> LensFacing.Unknown
    }

    private fun lensFacingSortKey(facing: LensFacing): Int = when (facing) {
        LensFacing.Back -> 0
        LensFacing.Front -> 1
        LensFacing.External -> 2
        LensFacing.Unknown -> 3
    }

}
