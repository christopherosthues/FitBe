package org.darthacheron.fitbe.nutrition.water

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Entity(tableName = "water_intake")
data class WaterIntakeEntity(
    @PrimaryKey val date: String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
    val amountMl: Int
)