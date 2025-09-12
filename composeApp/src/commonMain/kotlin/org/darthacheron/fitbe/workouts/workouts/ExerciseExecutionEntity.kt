package org.darthacheron.fitbe.workouts.workouts

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import org.darthacheron.fitbe.profile.ProfileEntity
import org.darthacheron.fitbe.workouts.exercises.ExerciseEntity

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "exercise_executions",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SetEntity::class,
            parentColumns = ["id"],
            childColumns = ["setId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["profileId"]),
        Index(value = ["exerciseId"]),
        Index(value = ["setId"])
    ]
)
data class ExerciseExecutionEntity(
    @PrimaryKey val id: Uuid,
    val profileId: Uuid,
    val exerciseId: Uuid,
    val timestamp: Instant,
    val repetitions: Int? = null,
    val weightKg: Double? = null,
    val distanceKm: Double? = null,
    val durationSeconds: Long? = null,
    val notes: String? = null,
    val setId: Uuid? = null, // Nullable foreign key
    // Added target value fields
    val targetRepetitions: Int? = null,
    val targetWeightKg: Double? = null,
    val targetDistanceKm: Double? = null,
    val targetDurationSeconds: Long? = null
) {
    fun toExerciseExecution(): ExerciseExecution {
        return ExerciseExecution(
            id = this.id,
            profileId = this.profileId,
            exerciseId = this.exerciseId,
            timestamp = this.timestamp,
            repetitions = this.repetitions,
            weightKg = this.weightKg,
            distanceKm = this.distanceKm,
            durationSeconds = this.durationSeconds,
            notes = this.notes,
            setId = this.setId,
            // Map target fields
            targetRepetitions = this.targetRepetitions,
            targetWeightKg = this.targetWeightKg,
            targetDistanceKm = this.targetDistanceKm,
            targetDurationSeconds = this.targetDurationSeconds
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun toEntity(exerciseExecution: ExerciseExecution): ExerciseExecutionEntity {
    return ExerciseExecutionEntity(
        id = exerciseExecution.id,
        profileId = exerciseExecution.profileId,
        exerciseId = exerciseExecution.exerciseId,
        timestamp = exerciseExecution.timestamp,
        repetitions = exerciseExecution.repetitions,
        weightKg = exerciseExecution.weightKg,
        distanceKm = exerciseExecution.distanceKm,
        durationSeconds = exerciseExecution.durationSeconds,
        notes = exerciseExecution.notes,
        setId = exerciseExecution.setId,
        // Map target fields
        targetRepetitions = exerciseExecution.targetRepetitions,
        targetWeightKg = exerciseExecution.targetWeightKg,
        targetDistanceKm = exerciseExecution.targetDistanceKm,
        targetDurationSeconds = exerciseExecution.targetDurationSeconds
    )
}

