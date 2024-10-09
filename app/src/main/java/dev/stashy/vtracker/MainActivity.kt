package dev.stashy.vtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dev.stashy.vtracker.tracking.FaceTracker
import dev.stashy.vtracker.ui.Main
import dev.stashy.vtracker.ui.vm.MainViewmodel

class MainActivity : ComponentActivity() {
    val vm by viewModels<MainViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val tracker = FaceTracker()
        vm.receiveEvents(tracker.results)

        setContent {
            Main()
        }
    }
}
