package dev.stashy.vtracker.model.tracking

import kotlin.time.Duration

data class TrackerFrame(
    val result: List<TrackingData>,
    val processingTime: Duration,
    val imageSize: Pair<Int, Int>,
)
