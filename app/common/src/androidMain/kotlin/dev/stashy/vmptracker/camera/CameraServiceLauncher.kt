package dev.stashy.vmptracker.camera

/**
 * Starts [dev.stashy.vmptracker.MainService] as a foreground service so camera
 * capture can run under the service lifecycle.
 */
fun interface CameraServiceLauncher {
    fun ensureStarted()
}

object NoOpCameraServiceLauncher : CameraServiceLauncher {
    override fun ensureStarted() {}
}
