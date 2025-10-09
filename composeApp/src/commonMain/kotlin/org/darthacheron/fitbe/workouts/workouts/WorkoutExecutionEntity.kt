package org.darthacheron.fitbe.workouts.workouts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import org.darthacheron.fitbe.profile.ProfileEntity // Assuming ProfileEntity exists
import org.darthacheron.fitbe.workouts.exercises.ExerciseEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "workout_executions",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProfileEntity::class, // Assuming ProfileEntity and its PK 'id'
            parentColumns = ["id"],
            childColumns = ["profile_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exercise_id"), Index("profile_id")]
)
data class WorkoutExecutionEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Uuid = Uuid.random(),
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Uuid,
    @ColumnInfo(name = "profile_id")
    val profileId: Uuid, // Assuming you have a profile system
    @ColumnInfo(name = "start_time_utc")
    val startTimeUtc: Instant,
    @ColumnInfo(name = "end_time_utc")
    val endTimeUtc: Instant? = null,
    @ColumnInfo(name = "status")
    val status: WorkoutExecutionStatus = WorkoutExecutionStatus.IN_PROGRESS,
    @ColumnInfo(name = "total_planned_sets")
    val totalPlannedSets: Int
)