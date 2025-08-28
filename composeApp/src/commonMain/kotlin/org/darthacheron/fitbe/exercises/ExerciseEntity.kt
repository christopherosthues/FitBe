package org.darthacheron.fitbe.exercises

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "exercises", indices = [Index(value = ["name"], unique = true)])
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid = Uuid.random(),
    @ColumnInfo(name = "name") val name: String,
    val guide: String,
    val default: Boolean = false
)
