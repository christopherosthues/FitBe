package org.darthacheron.fitbe.workouts.templates

import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class WorkoutTemplatesUiState(
    val isLoading: Boolean = true,
    val rawWorkoutTemplateList: List<WorkoutTemplate> = emptyList(),
    val favoriteWorkoutTemplateIds: Set<Uuid> = emptySet(),
    val workoutTemplateListError: StringResource? = null,
    val favoriteStateError: StringResource? = null
)