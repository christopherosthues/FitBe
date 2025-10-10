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
    UNKNOWN,
    WEIGHT_REPS,
    REPS_ONLY,
    TIMED,
    DISTANCE,
    WEIGHT_TIMED,
    WEIGHT_REPS_TIMED,
    DISTANCE_TIMED,
    COMPLETION_ONLY,
    OTHER;

    fun toStringResource(): StringResource =
        when (this) {
            UNKNOWN -> Res.string.exercise_type_unknown
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