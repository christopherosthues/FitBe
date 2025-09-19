package org.darthacheron.fitbe.workouts.workouts

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutExecutionWithSetsEntity(
    @Embedded
    val workoutExecution: WorkoutExecutionEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "workout_execution_id"
    )
    val sets: List<WorkoutSetExecutionEntity>
)
