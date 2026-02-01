package org.darthacheron.fitbe.health.sleep

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
    tableName = "sleeps",
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
data class SleepEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid = Uuid.random(),
    val profileId: Uuid,
    val startDateTime: Instant,
    val endDateTime: Instant,
    val lastModified: Instant?
) {
    fun toSleep(): Sleep =
        Sleep(
            id = id,
            profileId = profileId,
            start =
                startDateTime
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .toInstant(TimeZone.currentSystemDefault()),
            end =
                endDateTime
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .toInstant(TimeZone.currentSystemDefault()),
            lastModified = lastModified
        )
}

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
fun Sleep.toSleepEntity(): SleepEntity =
    SleepEntity(
        id = this.id,
        profileId = this.profileId,
        startDateTime = this.start.toLocalDateTime(TimeZone.UTC).toInstant(TimeZone.UTC),
        endDateTime = this.end.toLocalDateTime(TimeZone.UTC).toInstant(TimeZone.UTC),
        lastModified = this.lastModified
    )