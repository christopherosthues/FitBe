package org.darthacheron.fitbe.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_settings
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class SettingsViewModel(
    private val repository: SettingsRepository,
    val topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.Settings

    override val title: StringResource
        get() = Res.string.top_bar_title_settings

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

    fun setBodyMeasurementUnit(unit: BodyMeasurementUnit) {
        currentSettings = currentSettings.copy(bodyMeasurementUnit = unit)
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
