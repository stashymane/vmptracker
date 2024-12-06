package dev.stashy.vtracker.service.tracking

import android.content.Context
import android.graphics.Bitmap
import androidx.datastore.core.DataStore
import dev.stashy.vtracker.model.settings.FaceTrackerSettings
import dev.stashy.vtracker.model.settings.HandTrackerSettings
import dev.stashy.vtracker.model.settings.PoseTrackerSettings
import dev.stashy.vtracker.model.tracking.TrackerFrame
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.merge

private val logger = KotlinLogging.logger { }

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.startTrackers(
    context: Context,
    images: ReceiveChannel<Bitmap>,
    face: DataStore<FaceTrackerSettings>,
    hand: DataStore<HandTrackerSettings>,
    pose: DataStore<PoseTrackerSettings>,
): ReceiveChannel<Result<TrackerFrame>> =
    produce {
        logger.debug { "Fetching tracker settings..." }
        val faceSettings = face.data.firstOrNull() ?: FaceTrackerSettings()
        val handSettings = hand.data.firstOrNull() ?: HandTrackerSettings()
        val poseSettings = pose.data.firstOrNull() ?: PoseTrackerSettings()

        logger.debug { "Starting trackers..." }
        merge(
            if (faceSettings.enabled) faceTracker(context, images, faceSettings).consumeAsFlow()
            else emptyFlow(),
            if (handSettings.enabled) handTracker(context, images, handSettings).consumeAsFlow()
            else emptyFlow(),
            if (poseSettings.enabled) emptyFlow()
            else emptyFlow()
        ).collect(::send)
    }
