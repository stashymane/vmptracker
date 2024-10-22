package dev.stashy.vtracker.ui.vm

import androidx.lifecycle.ViewModel
import dev.stashy.vtracker.service.AppService

class MainViewmodel(
    service: AppService
) : ViewModel(), AppService by service {

}