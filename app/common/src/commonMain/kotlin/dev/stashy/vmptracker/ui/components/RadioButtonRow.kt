package dev.stashy.vmptracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TonalToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost

@Composable
fun <T> RadioButtonRow(
    options: List<T>,
    current: T?,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    render: @Composable (T) -> Unit
) {
    FlowRow(
        modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        options.forEachIndexed { index, item ->
            val selected = item == current

            TonalToggleButton(
                selected,
                { onClick(item) },
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    options.size - 1 -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                }
            ) {
                render(item)
            }
        }
    }
}

@ComponentPreview
@Composable
private fun RadioButtonRowPreview() = PreviewHost {
    RadioButtonRow(listOf(30, 60, 120), 60, {}) {
        Text("$it FPS")
    }
}
