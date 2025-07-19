package org.darthacheron.fitbe.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsViewModel : ViewModel(), KoinComponent {
    private val repository: SettingsRepository by inject()

    // Persistent settings
    private var persistedSettings by mutableStateOf(Settings())

    // Temporary settings for editing
    var currentSettings by mutableStateOf(Settings())
        private set

    init {
        viewModelScope.launch {
            repository.getSettingsFlow().collect {
                persistedSettings = it
                currentSettings = it
            }
        }
    }

    fun setWeightUnit(unit: WeightUnit) {
        currentSettings = currentSettings.copy(weightUnit = unit)
    }

    fun setDistanceUnit(unit: DistanceUnit) {
        currentSettings = currentSettings.copy(distanceUnit = unit)
    }

    fun setThemeMode(mode: ThemeMode) {
        currentSettings = currentSettings.copy(themeMode = mode)
    }

    fun saveSettings() {
        viewModelScope.launch {
            repository.saveSettings(currentSettings)
        }
    }

    fun resetToDefaults() {
        currentSettings = Settings() // Uses default values from data class
    }

    fun revertChanges() {
        currentSettings = persistedSettings
    }
}
