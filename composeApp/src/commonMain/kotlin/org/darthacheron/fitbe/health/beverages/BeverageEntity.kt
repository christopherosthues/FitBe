package org.darthacheron.fitbe.health.beverages

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import org.darthacheron.fitbe.profile.ProfileEntity

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
@Entity(
    tableName = "beverages",
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
data class BeverageEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid = Uuid.random(),
    val profileId: Uuid,
    val dateUtc: Instant = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.atStartOfDayIn(TimeZone.UTC),
    val amount: Int,
    val beverage: String,
    val unit: FluidUnit
) {
    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    fun toBeverage(): Beverage {
        return Beverage(
            id = id,
            profileId = profileId,
            dateUtc = dateUtc,
            amount = amount.toUInt(),
            beverage = beverage,
            unit = unit
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun Beverage.toBeverageEntity(): BeverageEntity {
    return BeverageEntity(
        id = this.id,
        profileId = this.profileId,
        dateUtc = this.dateUtc,
        amount = this.amount.toInt(),
        beverage = this.beverage,
        unit = this.unit
    )
}