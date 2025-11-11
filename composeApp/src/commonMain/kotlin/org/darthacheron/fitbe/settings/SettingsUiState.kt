package org.darthacheron.fitbe.settings

import org.darthacheron.fitbe.ui.UiState
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class SettingsUiState(
    isLoading: Boolean = false,
    val currentWeightUnit: WeightUnit = WeightUnit.KG,
    val currentDistanceUnit: DistanceUnit = DistanceUnit.KM,
    val currentBodyMeasurementUnit: BodyMeasurementUnit = BodyMeasurementUnit.CM,
    val currentThemeMode: ThemeMode = ThemeMode.SYSTEM,
    val persistedSettings: Settings = Settings(),
    error: SettingsError = SettingsError()
) : UiState<SettingsError>(isLoading, error) {

    fun copy(
        isLoading: Boolean = this.isLoading,
        currentWeightUnit: WeightUnit = this.currentWeightUnit,
        currentDistanceUnit: DistanceUnit = this.currentDistanceUnit,
        currentBodyMeasurementUnit: BodyMeasurementUnit = this.currentBodyMeasurementUnit,
        currentThemeMode: ThemeMode = this.currentThemeMode,
        persistedSettings: Settings = this.persistedSettings,
        error: SettingsError = this.error
    ): SettingsUiState = SettingsUiState(
        isLoading = isLoading,
        currentWeightUnit = currentWeightUnit,
        currentDistanceUnit = currentDistanceUnit,
        currentBodyMeasurementUnit = currentBodyMeasurementUnit,
        currentThemeMode = currentThemeMode,
        persistedSettings = persistedSettings,
        error = error
    )
}
