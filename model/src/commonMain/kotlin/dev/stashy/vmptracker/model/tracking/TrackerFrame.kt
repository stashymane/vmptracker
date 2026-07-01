package dev.stashy.vmptracker.model.tracking

import kotlin.time.Duration

data class TrackerFrame(
    val result: List<TrackingData> = listOf(),
    val processingTime: Duration = Duration.ZERO,
    val imageSize: Pair<Int, Int> = 0 to 0,
)
