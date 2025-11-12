package org.darthacheron.fitbe.settings.export

import org.darthacheron.fitbe.health.components.DialogUiState

data class ExportDialogUiState(
    val exportAll: Boolean = false,
    val exportBeverages: Boolean = false,
    val exportSleep: Boolean = false,
    val exportSteps: Boolean = false,
    val exportWeight: Boolean = false,
    val exportExercises: Boolean = false,
    val exportExercisesIncludeDefaults: Boolean = false,
    val exportEquipment: Boolean = false,
    val exportEquipmentIncludeDefaults: Boolean = false,
    val exportPath: String = ""
    // TODO: date range
) : DialogUiState {
    override val canSave: Boolean =
        (exportAll || exportBeverages || exportSleep || exportSteps || exportWeight || exportExercises || exportEquipment) && exportPath.isNotEmpty()
}