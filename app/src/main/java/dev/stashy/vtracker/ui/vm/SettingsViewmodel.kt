package dev.stashy.vtracker.ui.vm

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel

class SettingsViewmodel(val dataStore: DataStore<Preferences>) : ViewModel() {
    fun reload() {

    }
}