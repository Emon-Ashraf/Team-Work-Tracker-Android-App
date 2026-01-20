package com.example.teamworktracker.core.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session")

class SessionManager(private val context: Context) {

    private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")

    val accessToken: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[KEY_ACCESS_TOKEN] }

    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = token
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
