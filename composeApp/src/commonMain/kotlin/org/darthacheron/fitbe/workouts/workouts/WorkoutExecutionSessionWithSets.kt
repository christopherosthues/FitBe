package org.darthacheron.fitbe.workouts.workouts

import androidx.room.Embedded
import androidx.room.Relation
import kotlin.uuid.ExperimentalUuidApi

/**
 * Data class to hold a WorkoutExecutionSessionEntity and its related PerformedSetEntity objects.
 */
@OptIn(ExperimentalUuidApi::class)
data class WorkoutExecutionSessionWithSets(
    @Embedded
    val session: WorkoutExecutionSessionEntity,

    @Relation(
        parentColumn = "id", // Primary key of WorkoutExecutionSessionEntity
        entityColumn = "workoutExecutionSessionId" // Foreign key in PerformedSetEntity
    )
    val performedSets: List<PerformedSetEntity>
) {
    fun toWorkoutExecutionSession(): WorkoutExecutionSession {
        return session.toWorkoutExecutionSession().copy(
            performedSets = performedSets.map { it.toPerformedSet() }
        )
    }
}
