package org.darthacheron.fitbe.workouts.workouts

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import org.darthacheron.fitbe.profile.ProfileEntity
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateEntity

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "workout_execution_sessions",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WorkoutTemplateEntity::class, // Link to the new template table
            parentColumns = ["id"],
            childColumns = ["workoutTemplateId"],
            onDelete = ForeignKey.SET_NULL // Or CASCADE, depending on desired behavior
        )
    ],
    indices = [
        Index(value = ["profileId"]),
        Index(value = ["workoutTemplateId"])
    ]
)
data class WorkoutExecutionSessionEntity(
    @PrimaryKey val id: Uuid,
    val profileId: Uuid,
    val workoutTemplateId: Uuid? = null,
    val name: String, // Made non-null, can be derived from template or user input
    val startTimestamp: Instant,
    val endTimestamp: Instant? = null,
    val notes: String? = null
) {
    fun toWorkoutExecutionSession(): WorkoutExecutionSession {
        return WorkoutExecutionSession(
            id = this.id,
            profileId = this.profileId,
            workoutTemplateId = this.workoutTemplateId,
            name = this.name,
            startTimestamp = this.startTimestamp,
            endTimestamp = this.endTimestamp,
            notes = this.notes
            // performedSets will be loaded separately
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun WorkoutExecutionSession.toEntity(): WorkoutExecutionSessionEntity {
    return WorkoutExecutionSessionEntity(
        id = this.id,
        profileId = this.profileId,
        workoutTemplateId = this.workoutTemplateId,
        name = this.name,
        startTimestamp = this.startTimestamp,
        endTimestamp = this.endTimestamp,
        notes = this.notes
    )
}
