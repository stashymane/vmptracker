package dev.stashy.vmptracker.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.stashy.vmptracker.camera.CameraLens
import dev.stashy.vmptracker.camera.CameraLensState
import dev.stashy.vmptracker.camera.LensFacing
import dev.stashy.vmptracker.icons.Icons
import dev.stashy.vmptracker.icons.outlined.CameraSwitch24Dp
import dev.stashy.vmptracker.model.Screens
import dev.stashy.vmptracker.ui.LocalBackStack
import dev.stashy.vmptracker.ui.theme.ComponentPreview
import dev.stashy.vmptracker.ui.theme.PreviewHost
import org.jetbrains.compose.resources.stringResource
import vmptracker.app.Res
import vmptracker.app.camera_facing_external
import vmptracker.app.camera_facing_front
import vmptracker.app.camera_facing_rear
import vmptracker.app.camera_facing_unknown
import vmptracker.app.camera_lens_selector
import vmptracker.app.camera_lens_telephoto
import vmptracker.app.camera_lens_ultrawide
import vmptracker.app.camera_lens_wide
import vmptracker.app.camera_lens_zoom

@Composable
fun CameraLensSelector(
    lensState: CameraLensState,
    modifier: Modifier = Modifier,
) {
    if (lensState.lenses.size <= 1) return

    val backStack = LocalBackStack.current

    IconButton(
        onClick = { backStack.add(Screens.CameraLensPicker) },
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.75f)
        )
    ) {
        Icon(
            Icons.Outlined.CameraSwitch24Dp,
            stringResource(Res.string.camera_lens_selector),
        )
    }
}

@Composable
fun selectedCameraLensLabel(lensState: CameraLensState): String {
    val selectedLens = lensState.lenses.find { it.id == lensState.selectedId }
    return if (selectedLens != null) {
        cameraLensLabel(selectedLens)
    } else {
        stringResource(Res.string.camera_facing_unknown)
    }
}

@Composable
fun cameraLensLabel(lens: CameraLens): String {
    val facingLabel = when (lens.facing) {
        LensFacing.Front -> stringResource(Res.string.camera_facing_front)
        LensFacing.Back -> stringResource(Res.string.camera_facing_rear)
        LensFacing.External -> stringResource(Res.string.camera_facing_external)
        LensFacing.Unknown -> stringResource(Res.string.camera_facing_unknown)
    }

    if (lens.facing != LensFacing.Back) return facingLabel

    val typeLabel = when {
        lens.intrinsicZoomRatio < 0.9f -> stringResource(Res.string.camera_lens_ultrawide)
        lens.intrinsicZoomRatio > 1.1f -> stringResource(Res.string.camera_lens_telephoto)
        else -> stringResource(Res.string.camera_lens_wide)
    }

    val zoomLabel = formatIntrinsicZoomLabel(lens.intrinsicZoomRatio)
    return "$facingLabel · $typeLabel ($zoomLabel)"
}

@Composable
private fun formatIntrinsicZoomLabel(ratio: Float): String {
    val value = when {
        ratio < 1f -> "%.1f".format(ratio).removeSuffix(".0")
        ratio % 1f == 0f -> ratio.toInt().toString()
        else -> "%.1f".format(ratio)
    }
    return stringResource(Res.string.camera_lens_zoom, value)
}

@ComponentPreview
@Composable
private fun CameraLensSelectorPreview() = PreviewHost {
    CameraLensSelector(
        lensState = CameraLensState(
            lenses = listOf(
                CameraLens("0", LensFacing.Back, 0.5f),
                CameraLens("1", LensFacing.Back, 1f),
                CameraLens("2", LensFacing.Back, 2f),
                CameraLens("3", LensFacing.Front, 1f),
            ),
            selectedId = "1",
        ),
    )
}
