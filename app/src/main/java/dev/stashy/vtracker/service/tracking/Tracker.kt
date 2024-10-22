package dev.stashy.vtracker.service.tracking

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.channels.ReceiveChannel

interface Tracker<T, S> {
    val results: ReceiveChannel<T>

    fun start(context: Context, settings: S)
    fun stop()

    fun submit(frame: Bitmap)
}
