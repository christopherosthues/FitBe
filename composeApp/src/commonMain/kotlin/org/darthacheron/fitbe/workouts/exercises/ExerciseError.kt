package org.darthacheron.fitbe.workouts.exercises

import org.jetbrains.compose.resources.StringResource
import kotlin.math.exp

data class ExerciseError(
    val generalError: StringResource? = null,
    val nameError: StringResource? = null,
    val guideError: StringResource? = null,
    val muscleGroupError: StringResource? = null,
    val recommendedForError: StringResource? = null,
    val equipmentError: StringResource? = null,
    val exerciseTypeError: StringResource? = null
) {
    val hasGeneralError: Boolean
        get() = generalError != null

    val hasNameError: Boolean
        get() = nameError != null

    val hasGuideError: Boolean
        get() = guideError != null

    val hasMuscleGroupError: Boolean
        get() = muscleGroupError != null

    val hasRecommendedForError: Boolean
        get() = recommendedForError != null

    val hasEquipmentError: Boolean
        get() = equipmentError != null

    val hasExerciseTypeError: Boolean
        get() = exerciseTypeError != null

    val hasError: Boolean
        get() =
            hasGeneralError ||
                hasNameError ||
                hasGuideError ||
                hasMuscleGroupError ||
                hasEquipmentError ||
                hasRecommendedForError ||
                hasExerciseTypeError
}