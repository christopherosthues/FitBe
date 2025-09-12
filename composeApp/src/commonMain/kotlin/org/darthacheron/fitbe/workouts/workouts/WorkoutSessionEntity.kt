package org.darthacheron.fitbe.workouts.workouts

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import org.darthacheron.fitbe.profile.ProfileEntity

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "workout_sessions",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["profileId"])]
)
data class WorkoutSessionEntity(
    @PrimaryKey val id: Uuid,
    val profileId: Uuid,
    val startTimestamp: Instant,
    val endTimestamp: Instant? = null,
    val name: String? = null
) {
    fun toWorkoutSession(): WorkoutSession {
        return WorkoutSession(
            id = this.id,
            profileId = this.profileId,
            startTimestamp = this.startTimestamp,
            endTimestamp = this.endTimestamp,
            name = this.name
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun toEntity(workoutSession: WorkoutSession): WorkoutSessionEntity {
    return WorkoutSessionEntity(
        id = workoutSession.id,
        profileId = workoutSession.profileId,
        startTimestamp = workoutSession.startTimestamp,
        endTimestamp = workoutSession.endTimestamp,
        name = workoutSession.name
    )
}

