package dev.stashy.vmptracker.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import dev.stashy.vmptracker.ui.screen.CameraScreen
import dev.stashy.vmptracker.ui.screen.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(
    override val group: Group? = null
) : MultiBackStack.Entry<Screen.Group> {
    fun provideEntry(): NavEntry<Screen> = NavEntry(this) {
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

    @Serializable
    data object Settings : Screen(Group.Settings) {
        @Composable
        override fun Content() {
            SettingsScreen()
        }
    }
}
