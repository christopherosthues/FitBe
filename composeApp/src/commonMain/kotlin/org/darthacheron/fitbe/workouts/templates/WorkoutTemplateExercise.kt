package org.darthacheron.fitbe.workouts.templates

import org.darthacheron.fitbe.workouts.exercises.Exercise // Assuming you have an Exercise domain model
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class WorkoutTemplateExercise(
    val id: Uuid = Uuid(),
    val workoutTemplateId: Uuid,
    val exerciseId: Uuid, // Reference to the actual ExerciseEntity
    // val exercise: Exercise, // Optionally, you might want to hold the resolved Exercise object here
    val exerciseOrder: Int,
    val sets: List<WorkoutTemplateSet> = emptyList()
)
