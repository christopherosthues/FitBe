package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import org.darthacheron.fitbe.workouts.workouts.WorkoutSetStatus

class WorkoutSetStatusConverter {
    @TypeConverter
    fun fromWorkoutSetStatus(workoutSetStatus: WorkoutSetStatus): String = workoutSetStatus.name

    @TypeConverter
    fun toWorkoutSetStatus(value: String): WorkoutSetStatus = WorkoutSetStatus.valueOf(value)
}