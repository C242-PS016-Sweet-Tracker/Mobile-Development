package com.capstone.sweettrack.view.ui.setting

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class SettingViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {

    val reminderEat: LiveData<Boolean> = dataStore.data.map { preferences ->
        preferences[REMINDER_EAT_KEY] ?: false
    }.asLiveData()

    val reminderHealth: LiveData<Boolean> = dataStore.data.map { preferences ->
        preferences[REMINDER_HEALTH_KEY] ?: false
    }.asLiveData()

    fun setReminderEat(value: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[REMINDER_EAT_KEY] = value
            }
        }
    }

    fun setReminderHealth(value: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[REMINDER_HEALTH_KEY] = value
            }
        }
    }

    companion object {
        private val REMINDER_EAT_KEY = booleanPreferencesKey("reminder_eat")
        private val REMINDER_HEALTH_KEY = booleanPreferencesKey("reminder_health")
    }
}

