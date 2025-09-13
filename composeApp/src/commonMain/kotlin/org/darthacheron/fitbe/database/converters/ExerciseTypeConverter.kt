package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import org.darthacheron.fitbe.workouts.exercises.ExerciseType

class ExerciseTypeConverter {
    @TypeConverter
    fun fromExerciseType(value: ExerciseType?): String? = value?.name

    @TypeConverter
    fun toExerciseType(value: String?): ExerciseType? = value?.let { ExerciseType.valueOf(it) }
}