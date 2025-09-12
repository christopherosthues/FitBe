package org.darthacheron.fitbe.workouts.templates

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "workout_template_sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutTemplateExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutTemplateExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["workoutTemplateExerciseId"])]
)
data class WorkoutTemplateSetEntity(
    @PrimaryKey val id: Uuid,
    val workoutTemplateExerciseId: Uuid,
    val setOrder: Int,
    val targetRepetitions: Int? = null,
    val targetWeightKg: Double? = null,
    val targetDistanceKm: Double? = null,
    val targetDurationSeconds: Long? = null,
    val targetRestDurationSeconds: Int? = null, // Recommended rest time after this set
    val notes: String? = null
) {
    fun toWorkoutTemplateSet(): WorkoutTemplateSet {
        return WorkoutTemplateSet(
            id = this.id,
            workoutTemplateExerciseId = this.workoutTemplateExerciseId,
            setOrder = this.setOrder,
            targetRepetitions = this.targetRepetitions,
            targetWeightKg = this.targetWeightKg,
            targetDistanceKm = this.targetDistanceKm,
            targetDurationSeconds = this.targetDurationSeconds,
            targetRestDurationSeconds = this.targetRestDurationSeconds,
            notes = this.notes
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun WorkoutTemplateSet.toEntity(): WorkoutTemplateSetEntity {
    return WorkoutTemplateSetEntity(
        id = this.id,
        workoutTemplateExerciseId = this.workoutTemplateExerciseId,
        setOrder = this.setOrder,
        targetRepetitions = this.targetRepetitions,
        targetWeightKg = this.targetWeightKg,
        targetDistanceKm = this.targetDistanceKm,
        targetDurationSeconds = this.targetDurationSeconds,
        targetRestDurationSeconds = this.targetRestDurationSeconds,
        notes = this.notes
    )
}
