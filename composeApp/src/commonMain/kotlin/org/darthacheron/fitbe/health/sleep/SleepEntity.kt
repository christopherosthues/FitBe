package org.darthacheron.fitbe.health.sleep

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import org.darthacheron.fitbe.profile.ProfileEntity

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
    val start: Instant,
    val end: Instant,
) {
    fun toSleep(): Sleep {
        return Sleep(
            id = id,
            profileId = profileId,
            start = start.toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(TimeZone.currentSystemDefault()),
            end = end.toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(TimeZone.currentSystemDefault()),
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun Sleep.toSleepEntity(): SleepEntity {
    return SleepEntity(
        id = this.id,
        profileId = this.profileId,
        start = this.start.toLocalDateTime(TimeZone.UTC).toInstant(TimeZone.UTC),
        end = this.end.toLocalDateTime(TimeZone.UTC).toInstant(TimeZone.UTC),
    )
}