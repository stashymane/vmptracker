package dev.stashy.vmptracker.ui.nav

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MultiBackStack<Entry : MultiBackStack.Entry<Group>, Group : Any>(
    private val initial: Entry
) {
    interface Entry<T> {
        val group: T?
    }

    private val stacks = linkedMapOf<Group, MutableList<Entry>>()
    private val initialGroup =
        initial.group ?: throw IllegalArgumentException("Initial entry must have group")

    var currentGroup by mutableStateOf(initialGroup)
        private set
    private var currentStack = mutableListOf<Entry>()

    val backStack = mutableStateListOf(initial)

    fun add(entry: Entry) {
        val group = entry.group
        if (group != null && group != currentGroup)
            swapTo(group)

        if (currentStack.lastOrNull() == entry)
            return

        currentStack.add(entry)

        update()
    }

    fun removeLast() {
        currentStack.removeLastOrNull()
        if (currentStack.isEmpty())
            stacks.keys.lastOrNull()?.let(::swapTo)
        if (currentStack.isEmpty())
            reset()

        update()
    }

    fun replace(entry: Entry) {
        currentStack.removeLastOrNull()

        val group = entry.group
        if (group != null && group != currentGroup)
            swapTo(group)

        currentStack.add(entry)

        update()
    }

    fun swapToGroup(root: Entry) {
        val group = root.group ?: throw IllegalArgumentException("Entry must have group")
        swapTo(group)
        if (currentStack.isEmpty() && !(currentGroup == initialGroup && stacks.isEmpty()))
            currentStack.add(root)

        update()
    }

    private fun swapTo(group: Group) {
        if (currentStack.isNotEmpty())
            stacks[currentGroup] = currentStack
        currentStack = stacks.remove(group) ?: mutableListOf()
        currentGroup = group
    }

    private fun reset() {
        currentGroup = initialGroup
        currentStack = mutableListOf()
    }

    private fun update() {
        backStack.clear()
        backStack.addAll(listOf(initial) + stacks.flatMap(Map.Entry<Group, List<Entry>>::value) + currentStack)
    }

    fun isEmpty() = backStack.size <= 1
    fun isNotEmpty() = backStack.size > 1
}
