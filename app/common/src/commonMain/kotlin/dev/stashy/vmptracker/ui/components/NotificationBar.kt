package dev.stashy.vmptracker.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.stashy.battery.BatteryMonitor
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost

private fun <T> notificationTransitionSpec(): AnimatedContentTransitionScope<T>.() -> ContentTransform =
    { fadeIn() + scaleIn(initialScale = 0.9f) togetherWith fadeOut() + scaleOut(targetScale = 0.9f) using null }

@Composable
fun NotificationBar(
    modifier: Modifier = Modifier
) {
    val batteryState by BatteryMonitor.state.collectAsStateWithLifecycle()

    Column(
        modifier.fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeContent),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.End
    ) {
        BatteryStateNotification(batteryState)
    }
}

@Composable
private fun BatteryStateNotification(
    state: BatteryMonitor.State
) {
    val data = (state as? Available)?.data
    val isLow = data != null && data.state is Low && data.power is Discharging

    AnimatedContent(isLow, transitionSpec = notificationTransitionSpec()) { isLow ->
        if (isLow)
            BatteryLevelNotification()
    }
}

@ComponentPreview
@Composable
private fun NotificationBarPreview() = PreviewHost {
    NotificationBar()
}
