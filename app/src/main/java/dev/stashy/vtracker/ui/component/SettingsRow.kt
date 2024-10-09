package dev.stashy.vtracker.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em

@Composable
fun SettingsRow(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    titleIcon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    current: @Composable () -> Unit,
    control: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    Surface(onClick = onClick, modifier = modifier, enabled = enabled) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row {
                titleIcon?.let {
                    it()
                    Spacer(Modifier.width(8.dp))
                }
                Column(Modifier.weight(1f)) {
                    title()
                    ProvideTextStyle(
                        LocalTextStyle.current.copy(
                            fontSize = 3.em,
                            lineHeight = 1.2.em,
                            color = LocalContentColor.current.copy(alpha = 0.7f)
                        )
                    ) {
                        description.invoke()
                    }
                }
                Spacer(Modifier.width(16.dp))
                ProvideTextStyle(LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)) {
                    current()
                }
            }
            control?.let {
                Row {
                    it()
                }
            }
        }
    }
}