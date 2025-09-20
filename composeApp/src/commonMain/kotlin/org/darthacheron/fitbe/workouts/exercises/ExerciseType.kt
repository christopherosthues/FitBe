package org.darthacheron.fitbe.workouts.exercises

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.exercise_type_completion_only
import fitbe.composeapp.generated.resources.exercise_type_distance
import fitbe.composeapp.generated.resources.exercise_type_distance_timed
import fitbe.composeapp.generated.resources.exercise_type_other
import fitbe.composeapp.generated.resources.exercise_type_reps_only
import fitbe.composeapp.generated.resources.exercise_type_timed
import fitbe.composeapp.generated.resources.exercise_type_unknown
import fitbe.composeapp.generated.resources.exercise_type_weight_reps
import fitbe.composeapp.generated.resources.exercise_type_weight_reps_timed
import fitbe.composeapp.generated.resources.exercise_type_weight_timed
import org.jetbrains.compose.resources.StringResource

enum class ExerciseType {
    UNKNOWN, // Added UNKNOWN as the first value

    /** Exercises tracked by weight and repetitions (e.g., Bench Press, Squats with weights) */
    WEIGHT_REPS,

    /** Exercises tracked by repetitions only (e.g., Push Ups, Sit Ups, Bodyweight Squats) */
    REPS_ONLY,

    /** Exercises tracked by duration (e.g., Plank, Wall Sit, Stretching) */
    TIMED,

    /** Exercises tracked by distance and optionally duration (e.g., Running, Cycling, Rowing) */
    DISTANCE,

    /** Exercises tracked by weight and duration (e.g., Weighted Plank, Farmer's Walk) */
    WEIGHT_TIMED,

    /** Exercises tracked by weight, repetitions, and duration (e.g., AMRAP for X minutes with Y weight, or N reps with Z weight for time) */
    WEIGHT_REPS_TIMED,

    /** Exercises tracked by both distance and duration (e.g., Running 5km in 25 minutes, Cycling for 1 hour covering 20km) */
    DISTANCE_TIMED,

    /** Exercises where only completion is tracked, or the tracking type is not specific or varies greatly. */
    COMPLETION_ONLY,

    /** For any other type of exercise or for exercises where tracking metrics are not predefined. */
    OTHER;

    fun toStringResource(): StringResource {
        return when (this) {
            UNKNOWN -> Res.string.exercise_type_unknown // Added case for UNKNOWN
            WEIGHT_REPS -> Res.string.exercise_type_weight_reps
            REPS_ONLY -> Res.string.exercise_type_reps_only
            TIMED -> Res.string.exercise_type_timed
            DISTANCE -> Res.string.exercise_type_distance
            WEIGHT_TIMED -> Res.string.exercise_type_weight_timed
            WEIGHT_REPS_TIMED -> Res.string.exercise_type_weight_reps_timed
            DISTANCE_TIMED -> Res.string.exercise_type_distance_timed
            COMPLETION_ONLY -> Res.string.exercise_type_completion_only
            OTHER -> Res.string.exercise_type_other
        }
    }
}

