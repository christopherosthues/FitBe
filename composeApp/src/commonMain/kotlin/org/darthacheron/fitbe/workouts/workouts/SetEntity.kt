package org.darthacheron.fitbe.workouts.workouts

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import org.darthacheron.fitbe.workouts.exercises.ExerciseEntity

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSessionEntity::class, // To be defined next
            parentColumns = ["id"],
            childColumns = ["workoutSessionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["workoutSessionId"]),
        Index(value = ["exerciseId"])
    ]
)
data class SetEntity(
    @PrimaryKey val id: Uuid,
    val workoutSessionId: Uuid,
    val exerciseId: Uuid,
    val setOrder: Int
) {
    fun toExerciseSet(): ExerciseSet {
        return ExerciseSet(
            id = this.id,
            workoutSessionId = this.workoutSessionId,
            exerciseId = this.exerciseId,
            setOrder = this.setOrder
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun toEntity(exerciseSet: ExerciseSet): SetEntity {
    return SetEntity(
        id = exerciseSet.id,
        workoutSessionId = exerciseSet.workoutSessionId,
        exerciseId = exerciseSet.exerciseId,
        setOrder = exerciseSet.setOrder
    )
}
