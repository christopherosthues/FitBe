package org.darthacheron.fitbe.settings

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.settings_error_loading
import fitbe.composeapp.generated.resources.settings_error_saving
import fitbe.composeapp.generated.resources.top_bar_title_settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
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

    private val _uiState = MutableStateFlow(SettingsUiState(isLoading = true))
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository
                .getSettingsFlow()
                .catch { e ->
                    // Log error e
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = SettingsError(generalErrorMessage = Res.string.settings_error_loading)
                        )
                    }
                }.collect { loadedSettings ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentWeightUnit = loadedSettings.weightUnit,
                            currentDistanceUnit = loadedSettings.distanceUnit,
                            currentBodyMeasurementUnit = loadedSettings.bodyMeasurementUnit,
                            currentThemeMode = loadedSettings.themeMode,
                            persistedSettings = loadedSettings,
                            error = SettingsError()
                        )
                    }
                }
        }
    }

    fun onWeightUnitChanged(unit: WeightUnit) {
        _uiState.update { it.copy(currentWeightUnit = unit) }
    }

    fun onDistanceUnitChanged(unit: DistanceUnit) {
        _uiState.update { it.copy(currentDistanceUnit = unit) }
    }

    fun onBodyMeasurementUnitChanged(unit: BodyMeasurementUnit) {
        _uiState.update { it.copy(currentBodyMeasurementUnit = unit) }
    }

    fun onThemeModeChanged(mode: ThemeMode) {
        _uiState.update { it.copy(currentThemeMode = mode) }
    }

    fun saveSettings(onSuccess: () -> Unit = {}) {
        _uiState.update { it.copy(isLoading = true, error = SettingsError()) }
        viewModelScope.launch {
            try {
                val currentState = _uiState.value
                val settingsToSave =
                    Settings(
                        weightUnit = currentState.currentWeightUnit,
                        distanceUnit = currentState.currentDistanceUnit,
                        bodyMeasurementUnit = currentState.currentBodyMeasurementUnit,
                        themeMode = currentState.currentThemeMode,
                        selectedProfileId = currentState.persistedSettings.selectedProfileId
                    )
                repository.saveSettings(settingsToSave)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        persistedSettings = settingsToSave
                    )
                }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = SettingsError(generalErrorMessage = Res.string.settings_error_saving)
                    )
                }
            }
        }
    }

    fun resetToDefaults() {
        val defaultSettings = Settings()
        _uiState.update {
            it.copy(
                currentWeightUnit = defaultSettings.weightUnit,
                currentDistanceUnit = defaultSettings.distanceUnit,
                currentBodyMeasurementUnit = defaultSettings.bodyMeasurementUnit,
                currentThemeMode = defaultSettings.themeMode
            )
        }
    }

    fun revertChanges() {
        _uiState.update {
            it.copy(
                currentWeightUnit = it.persistedSettings.weightUnit,
                currentDistanceUnit = it.persistedSettings.distanceUnit,
                currentBodyMeasurementUnit = it.persistedSettings.bodyMeasurementUnit,
                currentThemeMode = it.persistedSettings.themeMode
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = SettingsError()) }
    }
}