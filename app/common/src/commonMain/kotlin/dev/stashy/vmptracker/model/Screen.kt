package dev.stashy.vmptracker.model

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.metadata
import dev.stashy.vmptracker.screens.camera.CameraScreen
import dev.stashy.vmptracker.screens.settings.SettingsScreen
import dev.stashy.vmptracker.screens.sheets.CameraFrameRatePickerSheet
import dev.stashy.vmptracker.screens.sheets.CameraLensPickerSheet
import dev.stashy.vmptracker.ui.nav.BottomSheetSceneStrategy
import dev.stashy.vmptracker.ui.nav.MultiBackStack
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(
    override val group: Group? = null
) : MultiBackStack.Entry<Screen.Group> {
    open fun metadata(): Map<String, Any> = emptyMap()

    fun provideEntry(): NavEntry<Screen> = NavEntry(
        key = this,
        metadata = metadata() + metadata {
            group?.let { put(Group.MetaKey, it) }
        },
    ) {
        Content()
    }

    @Composable
    abstract fun Content()

    @Serializable
    data class Group(
        val name: String,
        val order: Int? = null
    ) {
        fun towards(target: Group?): Int {
            val sourceOrder = order ?: return 0
            val targetOrder = target?.order ?: return 0

            return sourceOrder.compareTo(targetOrder)
        }

        companion object {
            val Home = Group("home")
            val Settings = Group("settings")
        }

        object MetaKey : NavMetadataKey<Group>
    }
}

object Screens {
    @Serializable
    data object Camera : Screen(Group.Home) {
        @Composable
        override fun Content() {
            CameraScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Serializable
    data object CameraLensPicker : Screen(Group.Home) {
        override fun metadata(): Map<String, Any> =
            BottomSheetSceneStrategy.bottomSheet(
                shiftUnderlyingContent = true,
                showScrim = false,
            )

        @Composable
        override fun Content() {
            CameraLensPickerSheet()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Serializable
    data object CameraFrameRatePicker : Screen(Group.Home) {
        override fun metadata(): Map<String, Any> =
            BottomSheetSceneStrategy.bottomSheet(
                shiftUnderlyingContent = true,
                showScrim = false,
            )

        @Composable
        override fun Content() {
            CameraFrameRatePickerSheet()
        }
    }

    @Serializable
    data object Settings : Screen(Group.Settings) {
        @Composable
        override fun Content() {
            SettingsScreen()
        }
    }
}
