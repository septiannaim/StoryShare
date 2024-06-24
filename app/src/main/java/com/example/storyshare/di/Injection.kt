package com.example.storyshare.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyshare.api.ApiConfig
import com.example.storyshare.data.UserRepository
import com.example.storyshare.data.pref.UserPreferences

// Extension property for DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storyshare")

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val userPreference = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(userPreference, apiService)
    }
}
