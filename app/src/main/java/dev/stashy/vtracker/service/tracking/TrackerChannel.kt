package dev.stashy.vtracker.service.tracking

import android.graphics.Bitmap
import dev.stashy.vtracker.model.tracking.TrackerFrame
import dev.stashy.vtracker.model.tracking.TrackingData
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

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> CoroutineScope.tracker(
    images: ReceiveChannel<Bitmap>,
    bufferSize: Int = 1,
    create: () -> T,
    close: T.(Throwable?) -> Unit,
    process: suspend T.(Bitmap) -> List<TrackingData>,
): ReceiveChannel<Result<TrackerFrame>> = produce {
    val processor = withContext(Dispatchers.IO) { create() }

    invokeOnClose { cause -> processor.close(cause) }

    images.consumeAsFlow().buffer(bufferSize, BufferOverflow.DROP_OLDEST).collect { image ->
        val timestamp = Clock.System.now()
        send(runCatching {
            val result = withContext(Dispatchers.IO) { processor.process(image) }
            val delta = Clock.System.now() - timestamp

            TrackerFrame(result, delta, image.width to image.height)
        })

        image.recycle()
    }
}
