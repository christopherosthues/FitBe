package org.darthacheron.fitbe.settings

import kotlinx.coroutines.flow.Flow

/**
 * Platform-independent interface for settings persistence
 */
interface SettingsRepository {
    suspend fun saveSettings(settings: Settings)
    fun getSettingsFlow(): Flow<Settings>
    suspend fun getSettings(): Settings
}

/**
 * Common constants for settings storage
 */
internal object SettingsKeys {
    const val FILE_NAME = "fitbe_settings"
    const val WEIGHT_UNIT = "weight_unit"
    const val DISTANCE_UNIT = "distance_unit"
    const val THEME_MODE = "theme_mode"
}

