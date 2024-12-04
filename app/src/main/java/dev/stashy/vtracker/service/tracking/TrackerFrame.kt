package dev.stashy.vtracker.service.tracking

import kotlin.time.Duration

data class TrackerFrame<T>(
    val result: T,
    val frametime: Duration,
    val size: Pair<Int, Int>,
)
