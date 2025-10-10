package org.darthacheron.fitbe.workouts.exercises

import org.darthacheron.fitbe.workouts.equipment.TrainingEquipment
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ExerciseDetailUiState(
    val name: String = "",
    val guide: String = "",
    val targetMuscleGroups: List<MuscleGroup> = emptyList(),
    val imageUri: String? = null,
    val equipmentList: List<TrainingEquipment> = emptyList(),
    val recommendedFor: List<RecommendedFor> = emptyList(),
    val exerciseType: ExerciseType = ExerciseType.UNKNOWN,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val exerciseId: Uuid? = null,
    val error: ExerciseError = ExerciseError(),
    val default: Boolean = false,
    val persistedDefaultName: String? = null,
    val persistedDefaultGuide: String? = null,
    val persistedDefaultImageUri: String? = null,
    val persistedDefaultMuscleGroups: List<MuscleGroup>? = null,
    val persistedDefaultEquipmentList: List<TrainingEquipment>? = null,
    val persistedDefaultRecommendedForList: List<RecommendedFor>? = null,
    val persistedDefaultExerciseType: ExerciseType? = null,
    val isModifiedFromPersistedDefault: Boolean = false,
    val isFavorite: Boolean = false
) {
    val canResetToDefault: Boolean
        get() = isEditing && exerciseId != null && default && isModifiedFromPersistedDefault

    val canDelete: Boolean
        get() = !isEditing && exerciseId != null && !default

    val canCancelEditing: Boolean
        get() = isEditing && exerciseId != null

    val canEdit: Boolean
        get() = !isEditing && exerciseId != null

    val isSaveButtonVisible: Boolean
        get() = isEditing

    val canSave: Boolean
        get() = !isLoading && !error.hasError

    val canStartWorkout: Boolean
        get() = !isEditing && exerciseId != null
}