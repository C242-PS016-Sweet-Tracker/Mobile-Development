package com.capstone.sweettrack.view.ui.setting

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import java.io.File

class MyApplication : Application() {
    val dataStore: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.create {
            File(filesDir, "settings.preferences_pb")
        }
    }
}
