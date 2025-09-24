package org.darthacheron.fitbe.health.sleep

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate
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
    val hours: Int,
    val minutes: Int,
    val dateUtc: LocalDate,
) {
    fun toSleep(): Sleep {
        return Sleep(
            id = id,
            profileId = profileId,
            hours = hours.toUInt(),
            minutes = minutes.toUInt(),
            dateUtc = dateUtc
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun Sleep.toSleepEntity(): SleepEntity {
    return SleepEntity(
        id = this.id,
        profileId = this.profileId,
        hours = this.hours.toInt(),
        minutes = this.minutes.toInt(),
        dateUtc = this.dateUtc
    )
}