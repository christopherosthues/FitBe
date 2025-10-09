package org.darthacheron.fitbe.workouts.workouts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "workout_set_executions",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExecutionEntity::class,
            parentColumns = ["id"],
            childColumns = ["workout_execution_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workout_execution_id")]
)
data class WorkoutSetExecutionEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Uuid = Uuid.random(),
    @ColumnInfo(name = "workout_execution_id")
    val workoutExecutionId: Uuid,
    @ColumnInfo(name = "set_number")
    val setNumber: Int,
    @ColumnInfo(name = "status")
    val status: WorkoutSetStatus,
    // Target values for this set
    @ColumnInfo(name = "target_reps")
    val targetReps: Int? = null,
    @ColumnInfo(name = "target_weight_kg")
    val targetWeightKg: Double? = null,
    @ColumnInfo(name = "target_duration_seconds")
    val targetDurationSeconds: Int? = null,
    @ColumnInfo(name = "target_distance_km")
    val targetDistanceKm: Double? = null,
    // Actual values for this set
    @ColumnInfo(name = "actual_reps")
    val actualReps: Int? = null,
    @ColumnInfo(name = "actual_weight_kg")
    val actualWeightKg: Double? = null,
    @ColumnInfo(name = "actual_duration_seconds")
    val actualDurationSeconds: Int? = null,
    @ColumnInfo(name = "actual_distance_km")
    val actualDistanceKm: Double? = null,
    @ColumnInfo(name = "rest_time_seconds_after_set")
    val restTimeSecondsAfterSet: Int? = null, // Duration of rest taken *after* this set
    @ColumnInfo(name = "notes")
    val notes: String? = null
)