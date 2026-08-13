package dev.stashy.vmptracker.model.settings

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryDataStore<T>(initial: T) : DataStore<T> {
    private val mutex = Mutex()
    private val state = MutableStateFlow(initial)

    override val data = state.asStateFlow()

    override suspend fun updateData(transform: suspend (t: T) -> T): T =
        mutex.withLock {
            transform(state.value).also { state.value = it }
        }
}
