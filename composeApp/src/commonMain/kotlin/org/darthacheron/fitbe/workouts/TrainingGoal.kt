package org.darthacheron.fitbe.workouts

import androidx.compose.runtime.Composable
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.training_goal_maximum_strength
import fitbe.composeapp.generated.resources.training_goal_muscle_building
import fitbe.composeapp.generated.resources.training_goal_strength_endurance
import org.jetbrains.compose.resources.StringResource

enum class TrainingGoal {
    MAXIMUM_STRENGTH,
    MUSCLE_BUILDING,
    STRENGTH_ENDURANCE;

    @Composable
    fun localizedString(): StringResource {
        return when (this) {
            MAXIMUM_STRENGTH -> Res.string.training_goal_maximum_strength
            MUSCLE_BUILDING -> Res.string.training_goal_muscle_building
            STRENGTH_ENDURANCE -> Res.string.training_goal_strength_endurance
        }
    }

    fun getDefaultBreakInSeconds(): Int {
        return when (this) {
            MAXIMUM_STRENGTH -> 120 // (up to 300)
            MUSCLE_BUILDING -> 60 // up to 180)
            STRENGTH_ENDURANCE -> 30 // (up to 120)
        }
    }
}