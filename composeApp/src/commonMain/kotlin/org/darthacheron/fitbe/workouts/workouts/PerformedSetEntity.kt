package org.darthacheron.fitbe.workouts.workouts

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import org.darthacheron.fitbe.workouts.exercises.ExerciseEntity

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "performed_sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExecutionSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutExecutionSessionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE // Or RESTRICT if exercises should not be deleted if performed
        )
    ],
    indices = [
        Index(value = ["workoutExecutionSessionId"]),
        Index(value = ["exerciseId"])
    ]
)
data class PerformedSetEntity(
    @PrimaryKey val id: Uuid,
    val workoutExecutionSessionId: Uuid,
    val exerciseId: Uuid,
    val setOrder: Int,
    val timestamp: Instant, // When the set was actually logged

    // Target metrics (can be copied from a template or set ad-hoc during execution)
    val targetRepetitions: Int? = null,
    val targetWeightKg: Double? = null,
    val targetDistanceKm: Double? = null,
    val targetDurationSeconds: Long? = null,

    // Actual performed metrics
    val actualRepetitions: Int? = null,
    val actualWeightKg: Double? = null,
    val actualDistanceKm: Double? = null,
    val actualDurationSeconds: Long? = null,

    val plannedRestDurationSeconds: Int? = null, // Rest time planned after this set

    val notes: String? = null
) {
    fun toPerformedSet(): PerformedSet {
        return PerformedSet(
            id = this.id,
            workoutExecutionSessionId = this.workoutExecutionSessionId,
            exerciseId = this.exerciseId,
            setOrder = this.setOrder,
            timestamp = this.timestamp,
            targetRepetitions = this.targetRepetitions,
            targetWeightKg = this.targetWeightKg,
            targetDistanceKm = this.targetDistanceKm,
            targetDurationSeconds = this.targetDurationSeconds,
            actualRepetitions = this.actualRepetitions,
            actualWeightKg = this.actualWeightKg,
            actualDistanceKm = this.actualDistanceKm,
            actualDurationSeconds = this.actualDurationSeconds,
            plannedRestDurationSeconds = this.plannedRestDurationSeconds,
            notes = this.notes
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun PerformedSet.toEntity(): PerformedSetEntity {
    return PerformedSetEntity(
        id = this.id,
        workoutExecutionSessionId = this.workoutExecutionSessionId,
        exerciseId = this.exerciseId,
        setOrder = this.setOrder,
        timestamp = this.timestamp,
        targetRepetitions = this.targetRepetitions,
        targetWeightKg = this.targetWeightKg,
        targetDistanceKm = this.targetDistanceKm,
        targetDurationSeconds = this.targetDurationSeconds,
        actualRepetitions = this.actualRepetitions,
        actualWeightKg = this.actualWeightKg,
        actualDistanceKm = this.actualDistanceKm,
        actualDurationSeconds = this.actualDurationSeconds,
        plannedRestDurationSeconds = this.plannedRestDurationSeconds,
        notes = this.notes
    )
}
