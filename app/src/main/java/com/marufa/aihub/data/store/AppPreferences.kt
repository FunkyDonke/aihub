package com.marufa.aihub.data.store

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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "aihub_settings")

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val ACTIVE_TAB_ID = stringPreferencesKey("active_tab_id")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val FONT_SIZE = intPreferencesKey("font_size")  // percent: 75, 100, 125, 150
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
    }

    val activeTabId: Flow<String> = context.dataStore.data
        .map { it[ACTIVE_TAB_ID] ?: "" }

    val darkTheme: Flow<Boolean?> = context.dataStore.data
        .map { it[DARK_THEME] }  // null = follow system

    val fontSize: Flow<Int> = context.dataStore.data
        .map { it[FONT_SIZE] ?: 100 }

    val isFirstLaunch: Flow<Boolean> = context.dataStore.data
        .map { it[FIRST_LAUNCH] ?: true }

    suspend fun setActiveTabId(id: String) {
        context.dataStore.edit { it[ACTIVE_TAB_ID] = id }
    }

    suspend fun setDarkTheme(dark: Boolean?) {
        context.dataStore.edit {
            if (dark == null) it.remove(DARK_THEME)
            else it[DARK_THEME] = dark
        }
    }

    suspend fun setFontSize(size: Int) {
        context.dataStore.edit { it[FONT_SIZE] = size }
    }

    suspend fun setFirstLaunchDone() {
        context.dataStore.edit { it[FIRST_LAUNCH] = false }
    }
}
