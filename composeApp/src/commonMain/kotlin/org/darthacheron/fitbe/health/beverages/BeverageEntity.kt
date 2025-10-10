package org.darthacheron.fitbe.health.beverages

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.profile.ProfileEntity
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
    val dateUtc: Instant,
    val amount: Double,
    val beverage: String,
    val unit: FluidUnit
) {
    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    fun toBeverage(): Beverage =
        Beverage(
            id = id,
            profileId = profileId,
            date = dateUtc.toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(TimeZone.currentSystemDefault()),
            amount = amount,
            beverage = beverage,
            unit = unit
        )
}

@OptIn(ExperimentalUuidApi::class)
fun Beverage.toBeverageEntity(): BeverageEntity =
    BeverageEntity(
        id = this.id,
        profileId = this.profileId,
        dateUtc = this.date.toLocalDateTime(TimeZone.UTC).toInstant(TimeZone.UTC),
        amount = this.amount,
        beverage = this.beverage,
        unit = this.unit
    )