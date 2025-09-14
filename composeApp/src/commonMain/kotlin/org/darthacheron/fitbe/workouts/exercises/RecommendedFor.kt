package org.darthacheron.fitbe.workouts.exercises

import androidx.compose.runtime.Composable
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.recommended_for_warmup
import fitbe.composeapp.generated.resources.recommended_for_workout
import org.jetbrains.compose.resources.StringResource

enum class RecommendedFor {
    Warmup,
    Workout
}

@Composable
fun RecommendedFor.localizedString(): StringResource {
    return when (this) {
        RecommendedFor.Warmup -> Res.string.recommended_for_warmup
        RecommendedFor.Workout -> Res.string.recommended_for_workout
    }
}