package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import org.darthacheron.fitbe.exercises.MuscleGroup

class MuscleGroupListConverter {
    @TypeConverter
    fun fromMuscleGroupList(muscleGroups: List<MuscleGroup>?): String? {
        return muscleGroups?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toMuscleGroupList(muscleGroupsString: String?): List<MuscleGroup>? {
        return muscleGroupsString?.split(',')?.mapNotNull {
            try {
                MuscleGroup.valueOf(it)
            } catch (e: IllegalArgumentException) {
                // Handle cases where an enum constant might have been removed or renamed
                // Or log a warning
                null
            }
        }
    }
}