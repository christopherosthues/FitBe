package org.darthacheron.fitbe.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last

/**
 * Platform-independent interface for settings persistence
 */
interface SettingsRepository {
    suspend fun saveSettings(settings: Settings)
    fun getSettingsFlow(): Flow<Settings>
    suspend fun getSettings(): Settings = getSettingsFlow().last()
}

