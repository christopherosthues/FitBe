package org.darthacheron.fitbe.health.weight

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.profile.ProfileEntity
import kotlin.time.Duration.Companion.hours
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "body_weights",
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

}

@OptIn(ExperimentalUuidApi::class)
fun BodyWeight.toBodyWeightEntity(): BodyWeightEntity = BodyWeightEntity(
    id = this.id,
    weightInKg = this.weightInKg,
    bodyFatPercentage = this.bodyFatPercentage,
    muscleMassInKg = this.muscleMassInKg,
    boneMassInKg = this.boneMassInKg,
    bodyWaterInPercentage = this.bodyWaterInPercentage,
    dateUtc = this.dateUtc.atStartOfDayIn(TimeZone.UTC).plus(12.hours),
    profileId = this.profileId
)