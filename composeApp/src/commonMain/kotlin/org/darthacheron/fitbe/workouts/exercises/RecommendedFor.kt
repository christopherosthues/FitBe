package org.darthacheron.fitbe.workouts.exercises

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.recommended_for_warmup
import fitbe.composeapp.generated.resources.recommended_for_workout
import org.jetbrains.compose.resources.StringResource

enum class RecommendedFor {
    Warmup,
    Workout;

    fun toStringResource(): StringResource =
        when (this) {
            Warmup -> Res.string.recommended_for_warmup
            Workout -> Res.string.recommended_for_workout
        }
}