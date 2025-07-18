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
    var settings by mutableStateOf(Settings())
        private set

    init {
        viewModelScope.launch {
            repository.getSettingsFlow().collect {
                settings = it
            }
        }
    }

    fun setWeightUnit(unit: WeightUnit) {
        viewModelScope.launch {
            settings.copy(weightUnit = unit).also {
                repository.saveSettings(it)
            }
        }
    }

    fun setDistanceUnit(unit: DistanceUnit) {
        viewModelScope.launch {
            settings.copy(distanceUnit = unit).also {
                repository.saveSettings(it)
            }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            settings.copy(themeMode = mode).also {
                repository.saveSettings(it)
            }
        }
    }
}
