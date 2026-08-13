package dev.stashy.vmptracker.ui.nav

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import dev.stashy.vmptracker.ui.LocalBottomSheetPeekOffset
import dev.stashy.vmptracker.ui.nav.BottomSheetSceneStrategy.Companion.bottomSheet
import kotlin.math.min
import kotlinx.coroutines.flow.distinctUntilChanged

/** An [OverlayScene] that renders an [entry] within a [ModalBottomSheet]. */
@OptIn(ExperimentalMaterial3Api::class)
internal data class BottomSheetScene<T : Any>(
    override val key: T,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val entry: NavEntry<T>,
    private val modalBottomSheetProperties: ModalBottomSheetProperties,
    private val shiftUnderlyingContent: Boolean,
    private val showScrim: Boolean,
    private val onBack: () -> Unit,
) : OverlayScene<T> {

    override val entries: List<NavEntry<T>> = listOf(entry)

    override val content: @Composable (() -> Unit) = {
        val lifecycleOwner = rememberLifecycleOwner()
        val sheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden)

        if (shiftUnderlyingContent) {
            PublishPeekOffset(sheetState)
        }

        ModalBottomSheet(
            onDismissRequest = onBack,
            sheetState = sheetState,
            scrimColor = if (showScrim) BottomSheetDefaults.ScrimColor else Color.Transparent,
            properties = modalBottomSheetProperties,
        ) {
            CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
                entry.Content()
            }
        }
    }
}

/**
 * Publishes a content offset of half the currently visible sheet height, capped at half the
 * window height. Driven by [SheetState] so the fullscreen scrim is not measured as the sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PublishPeekOffset(sheetState: SheetState) {
    val peekOffset = LocalBottomSheetPeekOffset.current
    val windowInfo = LocalWindowInfo.current

    LaunchedEffect(sheetState, peekOffset, windowInfo) {
        snapshotFlow {
            val sheetY = sheetOffsetOrNaN(sheetState)
            val screenHeight = windowInfo.containerSize.height.toFloat()
            if (sheetY.isNaN() || screenHeight <= 0f) {
                0f
            } else {
                val visibleHeight = (screenHeight - sheetY).coerceAtLeast(0f)
                min(visibleHeight / 2f, screenHeight / 2f)
            }
        }
            .distinctUntilChanged()
            .collect { peekOffset.px = it }
    }

    DisposableEffect(peekOffset) {
        onDispose { peekOffset.px = 0f }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun sheetOffsetOrNaN(sheetState: SheetState): Float =
    try {
        sheetState.requireOffset()
    } catch (_: IllegalStateException) {
        Float.NaN
    }

/**
 * A [SceneStrategy] that displays entries that have added [bottomSheet] to their [NavEntry.metadata]
 * within a [ModalBottomSheet] instance.
 *
 * This strategy should always be added before any non-overlay scene strategies.
 */
@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null
        val bottomSheetProperties = lastEntry.metadata[BottomSheetKey] ?: return null
        return bottomSheetProperties.let { properties ->
            @Suppress("UNCHECKED_CAST")
            BottomSheetScene(
                key = lastEntry.contentKey as T,
                previousEntries = entries.dropLast(1),
                overlaidEntries = entries.dropLast(1),
                entry = lastEntry,
                modalBottomSheetProperties = properties,
                shiftUnderlyingContent = lastEntry.metadata[ShiftUnderlyingContent] == true,
                showScrim = lastEntry.metadata[ShowScrim] != false,
                onBack = onBack
            )
        }
    }

    companion object {
        /**
         * Function to be called on the [NavEntry.metadata] to mark this entry as something that
         * should be displayed within a [ModalBottomSheet].
         *
         * @param modalBottomSheetProperties properties that should be passed to the containing
         * [ModalBottomSheet].
         * @param shiftUnderlyingContent when true, underlying content is shifted by half the
         * visible sheet height while peeking, capped at half the screen height.
         * @param showScrim when false, the modal scrim tint is omitted.
         */
        fun bottomSheet(
            modalBottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
            shiftUnderlyingContent: Boolean = false,
            showScrim: Boolean = true,
        ) =
            metadata {
                put(BottomSheetKey, modalBottomSheetProperties)
                if (shiftUnderlyingContent) put(ShiftUnderlyingContent, true)
                if (!showScrim) put(ShowScrim, false)
            }

        object BottomSheetKey : NavMetadataKey<ModalBottomSheetProperties>
        object ShiftUnderlyingContent : NavMetadataKey<Boolean>
        object ShowScrim : NavMetadataKey<Boolean>
    }
}
