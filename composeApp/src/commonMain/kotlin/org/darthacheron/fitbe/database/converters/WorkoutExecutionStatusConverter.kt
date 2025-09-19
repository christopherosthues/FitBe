package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import org.darthacheron.fitbe.workouts.workouts.WorkoutExecutionStatus

class WorkoutExecutionStatusConverter {
    @TypeConverter
    fun fromWorkoutExecutionStatus(workoutExecutionStatus: WorkoutExecutionStatus): String =
        workoutExecutionStatus.name

    @TypeConverter
    fun toWorkoutExecutionStatus(value: String): WorkoutExecutionStatus =
        WorkoutExecutionStatus.valueOf(value)
}