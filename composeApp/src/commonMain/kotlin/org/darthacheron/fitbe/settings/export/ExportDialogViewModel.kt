package org.darthacheron.fitbe.settings.export

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.darthacheron.fitbe.health.components.DialogViewModel

class ExportDialogViewModel : DialogViewModel<ExportDialogUiState>() {
    override val uiState = MutableStateFlow(ExportDialogUiState())

    override fun dismissDialog() {
        uiState.update { ExportDialogUiState() }
    }

    fun onExportAllChanged(isChecked: Boolean) {
        uiState.update {
            it.copy(
                exportAll = isChecked,
                exportBeverages = isChecked,
                exportSleep = isChecked,
                exportSteps = isChecked,
                exportWeight = isChecked,
                exportExercises = isChecked,
                exportFavoriteExercises = isChecked,
                exportEquipment = isChecked,
                exportFavoriteEquipment = isChecked
            )
        }
    }

    fun onExportBeveragesChanged(isChecked: Boolean) {
        uiState.update { updateExportAllState(it.copy(exportBeverages = isChecked)) }
    }

    fun onExportSleepChanged(isChecked: Boolean) {
        uiState.update { updateExportAllState(it.copy(exportSleep = isChecked)) }
    }

    fun onExportStepsChanged(isChecked: Boolean) {
        uiState.update { updateExportAllState(it.copy(exportSteps = isChecked)) }
    }

    fun onExportWeightChanged(isChecked: Boolean) {
        uiState.update { updateExportAllState(it.copy(exportWeight = isChecked)) }
    }

    fun onExportExercisesChanged(isChecked: Boolean) {
        uiState.update { updateExportAllState(it.copy(exportExercises = isChecked)) }
    }

    fun onExportExercisesIncludeDefaultsChanged(isChecked: Boolean) {
        uiState.update { it.copy(exportExercisesIncludeDefaults = isChecked) }
    }

    fun onExportFavoriteExercisesChanged(isChecked: Boolean) {
        uiState.update { it.copy(exportFavoriteExercises = isChecked) }
    }

    fun onExportEquipmentChanged(isChecked: Boolean) {
        uiState.update { updateExportAllState(it.copy(exportEquipment = isChecked)) }
    }

    fun onExportEquipmentIncludeDefaultsChanged(isChecked: Boolean) {
        uiState.update { it.copy(exportEquipmentIncludeDefaults = isChecked) }
    }

    fun onExportFavoriteEquipmentChanged(isChecked: Boolean) {
        uiState.update { it.copy(exportFavoriteEquipment = isChecked) }
    }

    fun onExportPathChanged(exportPath: String) {
        uiState.update { it.copy(exportPath = exportPath) }
    }

    private fun updateExportAllState(state: ExportDialogUiState): ExportDialogUiState {
        val allSelected = state.exportBeverages &&
                state.exportSleep &&
                state.exportSteps &&
                state.exportWeight &&
                state.exportExercises &&
                state.exportEquipment
        return state.copy(exportAll = allSelected)
    }
}