package org.darthacheron.fitbe.workouts.exercises

import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ExercisesScreenUiState(
    val isLoading: Boolean = true, // Combined loading state
    val rawExercises: List<Exercise> = emptyList(),
    val favoriteExerciseIds: Set<Uuid> = emptySet(),
    val exerciseListError: StringResource? = null,
    val favoriteStateError: StringResource? = null,
    val selectedMuscleGroups: Set<MuscleGroup> = emptySet(),
    val selectedRecommendedForItems: Set<RecommendedFor> = emptySet(),
    val selectedExerciseTypes: Set<ExerciseType> = emptySet()
)