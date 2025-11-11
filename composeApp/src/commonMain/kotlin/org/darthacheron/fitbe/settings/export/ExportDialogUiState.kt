package org.darthacheron.fitbe.settings.export

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.darthacheron.fitbe.health.components.DialogUiState
import org.darthacheron.fitbe.ui.UiState

data class ExportDialogUiState(
    val exportAll: Boolean = false,
    val exportBeverages: Boolean = false,
    val exportSleep: Boolean = false,
    val exportSteps: Boolean = false,
    val exportWeight: Boolean = false,
    val exportExercises: Boolean = false,
    val exportExercisesIncludeDefaults: Boolean = false,
    val exportEquipment: Boolean = false,
    val exportEquipmentIncludeDefaults: Boolean = false
) : DialogUiState {
    override val canSave: Boolean =
        exportAll || exportBeverages || exportSleep || exportSteps || exportWeight || exportExercises || exportEquipment
}