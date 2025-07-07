package org.darthacheron.fitbe.nutrition.water

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
@OptIn(ExperimentalUuidApi::class)
data class WaterConsumptionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Uuid,
)