package com.example.storyshare.data.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[LOGGED_IN_STATE] = true
        }
    }

    suspend fun saveUserData(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = user.name
            preferences[USER_TOKEN] = user.token
            preferences[LOGGED_IN_STATE] = user.isLogin
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun getUserData(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                name = preferences[USER_NAME] ?: "",
                token = preferences[USER_TOKEN] ?: "",
                isLogin = preferences[LOGGED_IN_STATE] ?: false
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_TOKEN = stringPreferencesKey("user_token")
        private val LOGGED_IN_STATE = booleanPreferencesKey("logged_in_state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
