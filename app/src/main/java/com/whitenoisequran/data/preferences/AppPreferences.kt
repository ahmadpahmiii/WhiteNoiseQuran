package com.whitenoisequran.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        val SELECTED_RECITER_ID = intPreferencesKey("selected_reciter_id")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val LAST_PLAYED_SURAH = intPreferencesKey("last_played_surah")
        val SLEEP_TIMER_MINUTES = intPreferencesKey("sleep_timer_minutes")
        val SHUFFLE_ENABLED = booleanPreferencesKey("shuffle_enabled")
    }

    val selectedReciterIdFlow: Flow<Int> = dataStore.data.map { preferences ->
        preferences[SELECTED_RECITER_ID] ?: 5 // Default to Misyari Al-Afasy (id=5)
    }

    val isOnboardingCompletedFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ONBOARDING_COMPLETED] ?: false
    }

    val lastPlayedSurahFlow: Flow<Int> = dataStore.data.map { preferences ->
        preferences[LAST_PLAYED_SURAH] ?: 1 // Default to Surah Al-Fatihah
    }

    val shuffleEnabledFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SHUFFLE_ENABLED] ?: false
    }

    suspend fun setSelectedReciterId(reciterId: Int) {
        dataStore.edit { preferences ->
            preferences[SELECTED_RECITER_ID] = reciterId
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setLastPlayedSurah(surahNumber: Int) {
        dataStore.edit { preferences ->
            preferences[LAST_PLAYED_SURAH] = surahNumber
        }
    }

    suspend fun setShuffleEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHUFFLE_ENABLED] = enabled
        }
    }
}
