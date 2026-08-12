package dev.stashy.vmptracker.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.camera.CameraZoomState
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import kotlin.math.abs

private const val ZoomStopSnapThreshold = 0.08f
private val ChipSize = 36.dp
private val ChipSpacing = 2.dp

@Composable
fun CameraZoomSelector(
    zoomState: CameraZoomState,
    onZoomSelected: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (zoomState.stops.size <= 1) return

    val stops = zoomState.stops
    val nearestStop = stops.minByOrNull { abs(it - zoomState.zoomRatio) }
    val isOnStop =
        nearestStop != null && abs(zoomState.zoomRatio - nearestStop) < ZoomStopSnapThreshold

    Box(
        modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.75f))
            .padding(4.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(ChipSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            stops.forEach { stop ->
                val isNearest = stop == nearestStop
                val label = when {
                    isNearest && !isOnStop -> formatZoomLabel(
                        zoomState.zoomRatio,
                        includeMultiplier = true
                    )

                    else -> formatZoomLabel(stop, includeMultiplier = false)
                }

                ZoomStopChip(
                    label = label,
                    selected = isNearest && isOnStop,
                    modifier = Modifier
                        .size(ChipSize)
                        .clickable { onZoomSelected(stop) },
                )
            }
        }
    }
}

@Composable
private fun ZoomStopChip(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val containerColor by animateColorAsState(if (selected) MaterialTheme.colorScheme.inverseSurface else Color.Transparent)
    val contentColor by animateColorAsState(if (selected) MaterialTheme.colorScheme.inverseOnSurface else LocalContentColor.current)

    Box(
        Modifier
            .clip(CircleShape)
            .then(modifier)
            .background(containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            color = contentColor,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}

private fun formatZoomLabel(ratio: Float, includeMultiplier: Boolean): String {
    val value = when {
        ratio < 1f -> "%.1f".format(ratio).removeSuffix(".0")
        ratio % 1f == 0f -> ratio.toInt().toString()
        else -> "%.1f".format(ratio)
    }

    return if (includeMultiplier) "${value}x" else value
}

@ComponentPreview
@Composable
private fun CameraZoomSelectorPreview() = PreviewHost {
    CameraZoomSelector(
        zoomState = CameraZoomState(
            zoomRatio = 1f,
            minZoomRatio = 0.5f,
            maxZoomRatio = 10f,
            stops = listOf(0.5f, 1f, 2f, 5f),
        ),
        onZoomSelected = {},
    )
}

@ComponentPreview
@Composable
private fun CameraZoomSelectorPinchPreview() = PreviewHost {
    CameraZoomSelector(
        zoomState = CameraZoomState(
            zoomRatio = 1.4f,
            minZoomRatio = 0.5f,
            maxZoomRatio = 10f,
            stops = listOf(0.5f, 1f, 2f, 5f),
        ),
        onZoomSelected = {},
    )
}
