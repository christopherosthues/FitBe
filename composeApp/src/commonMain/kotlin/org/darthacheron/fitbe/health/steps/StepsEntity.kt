@file:OptIn(ExperimentalTime::class)

package org.darthacheron.fitbe.health.steps

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import org.darthacheron.fitbe.profile.ProfileEntity
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Entity(
    tableName = "steps",
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
data class StepsEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid = Uuid.random(),
    val profileId: Uuid,
    val steps: Int,
    val dateUtc: Instant
) {
    fun toSteps(): Steps =
        Steps(
            id = id,
            profileId = profileId,
            steps = steps.toUInt(),
            date = dateUtc.toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(TimeZone.currentSystemDefault())
        )
}

@OptIn(ExperimentalUuidApi::class)
fun Steps.toStepsEntity(): StepsEntity =
    StepsEntity(
        id = this.id,
        profileId = this.profileId,
        steps = this.steps.toInt(),
        dateUtc = this.date.toLocalDateTime(TimeZone.UTC).toInstant(TimeZone.UTC)
    )