package org.darthacheron.fitbe.workouts.equipment

import org.jetbrains.compose.resources.StringResource

data class TrainingEquipmentError(
    val generalError: StringResource? = null,
    val nameError: StringResource? = null
) {
    val hasGeneralError: Boolean
        get() = generalError != null

    val hasNameError: Boolean
        get() = nameError != null

    val hasError: Boolean
        get() = hasGeneralError || hasNameError
}