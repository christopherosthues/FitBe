package org.darthacheron.fitbe.health.weight

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.hours
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "body_weights")
data class BodyWeightEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid = Uuid.random(),
    val profileId: Uuid,
    val dateUtc: Instant = Clock.System.now(),
    val weightInKg: Double,
    val bodyFatPercentage: Double?,
    val muscleMassInKg: Double?,
    val boneMassInKg: Double?,
    val bodyWaterInPercentage: Double?,
) {
    fun toBodyWeight(): BodyWeight = BodyWeight(
        id = id,
        weightInKg = weightInKg,
        bodyFatPercentage = bodyFatPercentage,
        muscleMassInKg = muscleMassInKg,
        boneMassInKg = boneMassInKg,
        bodyWaterInPercentage = bodyWaterInPercentage,
        dateUtc = dateUtc.toLocalDateTime(TimeZone.UTC).date,
        profileId = profileId
    )

    companion object {
        fun fromBodyWeight(bodyWeight: BodyWeight): BodyWeightEntity = BodyWeightEntity(
            id = bodyWeight.id,
            weightInKg = bodyWeight.weightInKg,
            bodyFatPercentage = bodyWeight.bodyFatPercentage,
            muscleMassInKg = bodyWeight.muscleMassInKg,
            boneMassInKg = bodyWeight.boneMassInKg,
            bodyWaterInPercentage = bodyWeight.bodyWaterInPercentage,
            dateUtc = bodyWeight.dateUtc.atStartOfDayIn(TimeZone.UTC).plus(12.hours),
            profileId = bodyWeight.profileId
        )
    }
}