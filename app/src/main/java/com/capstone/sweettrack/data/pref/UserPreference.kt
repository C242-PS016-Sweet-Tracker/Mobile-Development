package com.capstone.sweettrack.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USER_KEY] = user.userId
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            val userId = preferences[USER_KEY] ?: ""
            val token = preferences[TOKEN_KEY] ?: ""
            val isLogin = preferences[IS_LOGIN_KEY] ?: false
            UserModel(userId, "", token, isLogin)
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USER_KEY = stringPreferencesKey("userId")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}