package org.darthacheron.fitbe.workouts.equipment

import org.darthacheron.fitbe.workouts.exercises.Exercise
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class AddEditTrainingEquipmentUiState(
    val name: String = "",
    val imageUri: String? = null,
    val default: Boolean = false,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val equipmentId: Uuid? = null,
    val error: TrainingEquipmentError = TrainingEquipmentError(),
    val persistedDefaultName: String? = null,
    val persistedDefaultImageUri: String? = null,
    val isModifiedFromPersistedDefault: Boolean = false,
    val exercises: List<Exercise> = emptyList(),
    val isFavorite: Boolean = false
) {
    val canCancelEditing: Boolean
        get() = isEditing && equipmentId != null && !isModifiedFromPersistedDefault

    val canEdit: Boolean
        get() = !isEditing && equipmentId != null

    val canSave: Boolean
        get() = !isLoading && !error.hasError

    val canDelete: Boolean
        get() = !isEditing && equipmentId != null && !default

    val canResetToDefault: Boolean
        get() = isEditing && equipmentId != null && default && isModifiedFromPersistedDefault

    val isSaveButtonVisible: Boolean
        get() = isEditing
}