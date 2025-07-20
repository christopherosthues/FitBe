package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import org.darthacheron.fitbe.profile.Gender

class GenderConverter {
    @TypeConverter
    fun fromGender(gender: Gender): String = gender.toString()

    @TypeConverter
    fun toGender(value: String): Gender = Gender.valueOf(value)
}