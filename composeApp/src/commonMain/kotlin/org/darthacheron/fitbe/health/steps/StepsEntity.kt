package org.darthacheron.fitbe.health.steps

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "steps")
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
            steps = steps,
            dateUtc = dateUtc,
        )
    }

}

@OptIn(ExperimentalUuidApi::class)
fun toEntity(steps: Steps): StepsEntity {
    return StepsEntity(
        id = steps.id,
        profileId = steps.profileId,
        steps = steps.steps,
        dateUtc = steps.dateUtc,
    )
}
