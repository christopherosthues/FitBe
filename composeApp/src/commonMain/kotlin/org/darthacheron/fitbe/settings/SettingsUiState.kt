package org.darthacheron.fitbe.settings

import org.darthacheron.fitbe.ui.UiState

class SettingsUiState(
    isLoading: Boolean = false,
    val currentWeightUnit: WeightUnit = WeightUnit.KG,
    val currentDistanceUnit: DistanceUnit = DistanceUnit.KM,
    val currentBodyMeasurementUnit: BodyMeasurementUnit = BodyMeasurementUnit.CM,
    val currentThemeMode: ThemeMode = ThemeMode.SYSTEM,
    val persistedSettings: Settings = Settings(),
    error: SettingsError = SettingsError(),

    val exportAll: Boolean = false,
    val exportBeverages: Boolean = false,
    val exportSleep: Boolean = false,
    val exportSteps: Boolean = false,
    val exportWeight: Boolean = false,
    val exportExercises: Boolean = false,
    val exportExercisesIncludeDefaults: Boolean = false,
    val exportEquipment: Boolean = false,
    val exportEquipmentIncludeDefaults: Boolean = false
) : UiState<SettingsError>(isLoading, error) {

    fun copy(
        isLoading: Boolean = this.isLoading,
        currentWeightUnit: WeightUnit = this.currentWeightUnit,
        currentDistanceUnit: DistanceUnit = this.currentDistanceUnit,
        currentBodyMeasurementUnit: BodyMeasurementUnit = this.currentBodyMeasurementUnit,
        currentThemeMode: ThemeMode = this.currentThemeMode,
        persistedSettings: Settings = this.persistedSettings,
        error: SettingsError = this.error,
        exportAll: Boolean = this.exportAll,
        exportBeverages: Boolean = this.exportBeverages,
        exportSleep: Boolean = this.exportSleep,
        exportSteps: Boolean = this.exportSteps,
        exportWeight: Boolean = this.exportWeight,
        exportExercises: Boolean = this.exportExercises,
        exportExercisesIncludeDefaults: Boolean = this.exportExercisesIncludeDefaults,
        exportEquipment: Boolean = this.exportEquipment,
        exportEquipmentIncludeDefaults: Boolean = this.exportEquipmentIncludeDefaults
    ): SettingsUiState = SettingsUiState(
        isLoading = isLoading,
        currentWeightUnit = currentWeightUnit,
        currentDistanceUnit = currentDistanceUnit,
        currentBodyMeasurementUnit = currentBodyMeasurementUnit,
        currentThemeMode = currentThemeMode,
        persistedSettings = persistedSettings,
        error = error,
        exportAll = exportAll,
        exportBeverages = exportBeverages,
        exportSleep = exportSleep,
        exportSteps = exportSteps,
        exportWeight = exportWeight,
        exportExercises = exportExercises,
        exportExercisesIncludeDefaults = exportExercisesIncludeDefaults,
        exportEquipment = exportEquipment,
        exportEquipmentIncludeDefaults = exportEquipmentIncludeDefaults
    )
}
