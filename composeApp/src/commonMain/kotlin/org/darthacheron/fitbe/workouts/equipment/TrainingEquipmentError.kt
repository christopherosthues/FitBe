package org.darthacheron.fitbe.workouts.equipment

import org.jetbrains.compose.resources.StringResource

data class TrainingEquipmentError(
    val hasGeneralError: Boolean = false,
    val generalError: StringResource? = null,
    val hasNameError: Boolean = false,
    val nameError: StringResource? = null
) {
    val hasError: Boolean
        get() = hasGeneralError || hasNameError
}