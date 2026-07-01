package dev.stashy.vmptracker.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.stashy.vmptracker.icons.Icons
import dev.stashy.vmptracker.icons.outlined.Face24Dp
import dev.stashy.vmptracker.icons.outlined.FrontHand24Dp
import dev.stashy.vmptracker.icons.outlined.Visibility24Dp
import dev.stashy.vmptracker.ui.theme.DevicePreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import dev.stashy.vmptracker.ui.theme.inDp
import dev.stashy.vmptracker.vm.SettingsViewmodel
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import vmptracker.app.Res
import vmptracker.app.screen_title_settings
import vmptracker.app.settings_face_section
import vmptracker.app.settings_hand_section

@Composable
fun SettingsScreen(vm: SettingsViewmodel = koinViewModel()) {
    val scrollState = rememberScrollState()

    Scaffold { paddingValues ->
        Column(Modifier.verticalScroll(scrollState).padding(paddingValues)) {
            SettingsSectionHeader(Modifier.padding(vertical = 8.dp)) {
                Text(
                    stringResource(Res.string.screen_title_settings),
                    style = MaterialTheme.typography.displaySmallEmphasized
                )
            }

            SettingsSectionHeader {
                SettingsSectionTitle(Res.string.settings_face_section, Icons.Outlined.Face24Dp)
            }

            SettingEntry(
                Res.string.settings_face_section,
                Modifier.clickable {},
                icon = Icons.Outlined.Visibility24Dp,
                subtitle = Res.string.settings_face_section
            ) {
                Switch(false, {})
            }

            SettingsSectionHeader {
                SettingsSectionTitle(Res.string.settings_hand_section, Icons.Outlined.FrontHand24Dp)
            }
        }
    }
}

@Composable
fun SettingsSectionHeader(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val lineHeight = LocalTextStyle.current.lineHeight.inDp()

    Row(
        modifier.padding(horizontal = 16.dp, vertical = lineHeight * 0.75f),
        horizontalArrangement = Arrangement.spacedBy(lineHeight * 0.75f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
            content()
        }
    }
}

@Composable
fun SettingsSectionTitle(
    title: StringResource,
    icon: ImageVector? = null
) {
    val lineHeight = LocalTextStyle.current.lineHeight.inDp()

    icon?.let {
        Icon(icon, null, Modifier.size(lineHeight))
    }
    Text(stringResource(title))
}

@Composable
fun SettingEntry(
    title: StringResource,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    subtitle: StringResource? = null,
    control: @Composable () -> Unit
) = Column(modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth()) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            icon?.let { icon -> Icon(icon, null) }
            Text(stringResource(title), style = MaterialTheme.typography.bodyLarge)
        }

        control()
    }
    subtitle?.let {
        Row(Modifier.padding(bottom = 8.dp).padding(horizontal = 8.dp)) {
            Text(
                stringResource(subtitle),
                style = MaterialTheme.typography.bodyMedium.merge(
                    color = LocalContentColor.current.copy(
                        alpha = 0.8f
                    )
                )
            )
        }
    }
}

@DevicePreview
@Composable
private fun SettingsScreenPreview() = PreviewHost {
    SettingsScreen(viewModel())
}
