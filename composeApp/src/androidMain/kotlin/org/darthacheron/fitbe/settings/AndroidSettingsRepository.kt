package org.darthacheron.fitbe.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SettingsKeys.FILE_NAME)

class AndroidSettingsRepository(
    private val context: Context
) : SettingsRepository {
    private object PreferencesKeys {
        val WEIGHT_UNIT = stringPreferencesKey(SettingsKeys.WEIGHT_UNIT)
        val DISTANCE_UNIT = stringPreferencesKey(SettingsKeys.DISTANCE_UNIT)
        val THEME_MODE = stringPreferencesKey(SettingsKeys.THEME_MODE)
    }

    override suspend fun saveSettings(settings: Settings) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.WEIGHT_UNIT] = settings.weightUnit.name
            preferences[PreferencesKeys.DISTANCE_UNIT] = settings.distanceUnit.name
            preferences[PreferencesKeys.THEME_MODE] = settings.themeMode.name
        }
    }

    override fun getSettingsFlow(): Flow<Settings> {
        return context.dataStore.data
            .catch { exception ->
                // Log the error and emit default settings
                exception.printStackTrace()
                emit(emptyPreferences())
            }
            .map { preferences ->
                Settings(
                    weightUnit = preferences[PreferencesKeys.WEIGHT_UNIT]?.let {
                        WeightUnit.valueOf(it)
                    } ?: WeightUnit.KG,
                    distanceUnit = preferences[PreferencesKeys.DISTANCE_UNIT]?.let {
                        DistanceUnit.valueOf(it)
                    } ?: DistanceUnit.KM,
                    themeMode = preferences[PreferencesKeys.THEME_MODE]?.let {
                        ThemeMode.valueOf(it)
                    } ?: ThemeMode.SYSTEM
                )
            }
    }

    override suspend fun getSettings(): Settings = getSettingsFlow().map { it }.single()
}

