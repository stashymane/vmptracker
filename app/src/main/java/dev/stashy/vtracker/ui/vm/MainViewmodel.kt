package dev.stashy.vtracker.ui.vm

import androidx.lifecycle.ViewModel
import dev.stashy.vtracker.service.TrackerService

class MainViewmodel(
    tracker: TrackerService
) : ViewModel(), TrackerService by tracker {}