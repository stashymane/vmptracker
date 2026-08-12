package dev.stashy.vmptracker.ui.nav

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import dev.stashy.vmptracker.ui.screen.CameraFrameRatePickerSheet
import dev.stashy.vmptracker.ui.screen.CameraLensPickerSheet
import dev.stashy.vmptracker.ui.screen.CameraScreen
import dev.stashy.vmptracker.ui.screen.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(
    override val group: Group? = null
) : MultiBackStack.Entry<Screen.Group> {
    fun provideEntry(): NavEntry<Screen> = NavEntry(
        key = this,
        metadata = entryMetadata(),
    ) {
        Content()
    }

    open fun entryMetadata(): Map<String, Any> = emptyMap()

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

            const val META_KEY: String = "screen.group"
        }
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
        override fun entryMetadata(): Map<String, Any> =
            BottomSheetSceneStrategy.bottomSheet()

        @Composable
        override fun Content() {
            CameraLensPickerSheet()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Serializable
    data object CameraFrameRatePicker : Screen(Group.Home) {
        override fun entryMetadata(): Map<String, Any> =
            BottomSheetSceneStrategy.bottomSheet()

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
