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
    fun toStringResource(): StringResource {
        return when (this) {
            MAXIMUM_STRENGTH -> Res.string.training_goal_maximum_strength
            MUSCLE_BUILDING -> Res.string.training_goal_muscle_building
            STRENGTH_ENDURANCE -> Res.string.training_goal_strength_endurance
        }
    }

    fun getDefaultBreakInSeconds(): IntRange {
        return when (this) {
            MAXIMUM_STRENGTH -> IntRange(120, 300)
            MUSCLE_BUILDING -> IntRange(60, 180)
            STRENGTH_ENDURANCE -> IntRange(30, 120)
        }
    }

    fun getDefaultNumberOfSets(): IntRange {
        return when (this) {
            MAXIMUM_STRENGTH -> IntRange(1, 5)
            MUSCLE_BUILDING -> IntRange(3, 5)
            STRENGTH_ENDURANCE -> IntRange(2, 4)
        }
    }
}