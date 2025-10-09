package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import org.darthacheron.fitbe.workouts.exercises.MuscleGroup

class MuscleGroupListConverter {
    @TypeConverter
    fun fromMuscleGroupList(muscleGroups: List<MuscleGroup>?): String? =
        muscleGroups?.joinToString(",") { it.name }

    @TypeConverter
    fun toMuscleGroupList(muscleGroupsString: String?): List<MuscleGroup>? =
        muscleGroupsString?.split(',')?.mapNotNull {
            try {
                MuscleGroup.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
}