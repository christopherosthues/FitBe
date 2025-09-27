package org.darthacheron.fitbe.workouts.templates

import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class WorkoutTemplateDetailUiState(
    val templateId: Uuid? = null,
    val name: String = "",
    val description: String? = null,
    val imageUri: String? = null,
    val warmups: List<WorkoutTemplateExerciseWithDetails> = emptyList(),
    val workouts: List<WorkoutTemplateExerciseWithDetails> = emptyList(),
    val default: Boolean = false,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val error: StringResource? = null,
    val isFavorite: Boolean = false,
    // TODO: Add persistedDefault values if reset functionality is needed for templates
    // val persistedDefaultName: String? = null,
    // val persistedDefaultDescription: String? = null,
    // val persistedDefaultImageUri: String? = null,
    // val isModifiedFromPersistedDefault: Boolean = false
)