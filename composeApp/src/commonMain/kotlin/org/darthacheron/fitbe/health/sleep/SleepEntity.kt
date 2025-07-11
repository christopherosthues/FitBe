package org.darthacheron.fitbe.health.sleep

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "sleeps")
data class SleepEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid = Uuid.random(),
    val hours: Int,
    val minutes: Int,
    val dateUtc: String,
)
