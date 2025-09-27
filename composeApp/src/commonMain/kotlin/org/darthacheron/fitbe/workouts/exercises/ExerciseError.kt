package org.darthacheron.fitbe.workouts.exercises

import org.jetbrains.compose.resources.StringResource

data class ExerciseError(
    val hasGeneralError: Boolean = false,
    val generalError: StringResource? = null,
    val hasNameError: Boolean = false,
    val nameError: StringResource? = null,
    val hasGuideError: Boolean = false,
    val guideError: StringResource? = null,
    val hasMuscleGroupError: Boolean = false,
    val muscleGroupError: StringResource? = null,
    val hasRecommendedForError: Boolean = false,
    val recommendedForError: StringResource? = null,
    val hasEquipmentError: Boolean = false,
    val equipmentError: StringResource? = null,
    val hasExerciseTypeError: Boolean = false,
    val exerciseTypeError: StringResource? = null
) {
    val hasError: Boolean
        get() = hasGeneralError || hasNameError || hasGuideError || hasMuscleGroupError || hasEquipmentError || hasRecommendedForError || hasExerciseTypeError
}