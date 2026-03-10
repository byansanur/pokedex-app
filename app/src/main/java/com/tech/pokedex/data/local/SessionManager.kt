package com.tech.pokedex.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    companion object {
        val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val KEY_USER_ID = stringPreferencesKey("user_id")
        val KEY_LOGIN_METHOD = stringPreferencesKey("login_method")
        val KEY_LAST_LOGGED_IN_USER_ID = stringPreferencesKey("last_logged_in_user_id")
        val KEY_LAST_ACTIVE_TIME = longPreferencesKey("last_active_time")
        const val SESSION_TIMEOUT = 30 * 60 * 1000L
    }

    suspend fun saveSession(userId: String, loginMethod: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_IS_LOGGED_IN] = true
            preferences[KEY_USER_ID] = userId
            preferences[KEY_LOGIN_METHOD] = loginMethod
            preferences[KEY_LAST_LOGGED_IN_USER_ID] = userId
            preferences[KEY_LAST_ACTIVE_TIME] = System.currentTimeMillis()
        }
    }

    suspend fun updateLastActiveTime() {
        context.dataStore.edit { preferences ->
            preferences[KEY_LAST_ACTIVE_TIME] = System.currentTimeMillis()
        }
    }

    val isSessionExpired: Flow<Boolean> = context.dataStore.data.map { preferences ->
        val lastActive = preferences[KEY_LAST_ACTIVE_TIME] ?: 0L
        if (lastActive == 0L) return@map false

        val now = System.currentTimeMillis()
        (now - lastActive) > SESSION_TIMEOUT
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences[KEY_IS_LOGGED_IN] = false
            preferences.remove(KEY_USER_ID)
            preferences.remove(KEY_LOGIN_METHOD)
            preferences.remove(KEY_LAST_ACTIVE_TIME)
        }
    }

    // Mengecek apakah ada user yang sedang aktif (masuk)
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_IS_LOGGED_IN] ?: false
    }

    // Mengambil ID User yang sedang aktif (untuk request API atau ambil Profile)
    val activeUserId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_USER_ID]
    }

    // Mengambil metode login yang digunakan user saat ini
    val loginMethod: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_LOGIN_METHOD]
    }

    // Mengambil ID User yang terakhir kali login (untuk dicek di layar Login)
    val lastLoggedInUserId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_LAST_LOGGED_IN_USER_ID]
    }
}