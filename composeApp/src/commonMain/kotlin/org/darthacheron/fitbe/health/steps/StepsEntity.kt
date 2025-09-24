package org.darthacheron.fitbe.health.steps

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.profile.ProfileEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
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
    val dateUtc: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
) {
    fun toSteps(): Steps {
        return Steps(
            id = id,
            profileId = profileId,
            steps = steps.toUInt(),
            dateUtc = dateUtc,
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun Steps.toStepsEntity(): StepsEntity {
    return StepsEntity(
        id = this.id,
        profileId = this.profileId,
        steps = this.steps.toInt(),
        dateUtc = this.dateUtc,
    )
}
