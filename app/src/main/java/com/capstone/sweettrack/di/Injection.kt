package com.capstone.sweettrack.di

import android.content.Context
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.local.room.SweetTrackDatabase
import com.capstone.sweettrack.data.pref.UserPreference
import com.capstone.sweettrack.data.pref.dataStore
import com.capstone.sweettrack.data.remote.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val sweetTrackDatabase = SweetTrackDatabase.getInstance(context)
        return Repository.getInstance(apiService, pref, sweetTrackDatabase)
    }
}