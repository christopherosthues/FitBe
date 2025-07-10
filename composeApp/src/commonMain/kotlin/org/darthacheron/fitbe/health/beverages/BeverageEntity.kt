package org.darthacheron.fitbe.health.beverages

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
@Entity(tableName = "beverages")
data class BeverageEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid = Uuid.random(),
    val date: String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
    val amount: Int,
    val beverage: String,
    val unit: FluidUnit
)