package dev.stashy.vmptracker.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSectionContent(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        Modifier.padding(horizontal = 16.dp).clip(MaterialTheme.shapes.large),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        content()
    }
}

