package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UuidConverter {
    @TypeConverter
    fun fromUUID(uuid: Uuid): String {
        return uuid.toString()
    }

    @TypeConverter
    fun uuidFromString(string: String?): Uuid {
        return Uuid.parse(string ?: "")
    }
}