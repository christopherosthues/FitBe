package org.darthacheron.fitbe.workouts.exercises

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.muscle_group_abs
import fitbe.composeapp.generated.resources.muscle_group_adductors
import fitbe.composeapp.generated.resources.muscle_group_back
import fitbe.composeapp.generated.resources.muscle_group_biceps
import fitbe.composeapp.generated.resources.muscle_group_calves
import fitbe.composeapp.generated.resources.muscle_group_cardio
import fitbe.composeapp.generated.resources.muscle_group_chest
import fitbe.composeapp.generated.resources.muscle_group_forearms
import fitbe.composeapp.generated.resources.muscle_group_full_body
import fitbe.composeapp.generated.resources.muscle_group_glutes
import fitbe.composeapp.generated.resources.muscle_group_hamstrings
import fitbe.composeapp.generated.resources.muscle_group_other
import fitbe.composeapp.generated.resources.muscle_group_quads
import fitbe.composeapp.generated.resources.muscle_group_shoulders
import fitbe.composeapp.generated.resources.muscle_group_triceps
import org.jetbrains.compose.resources.StringResource

enum class MuscleGroup {
    CHEST,
    BACK,
    SHOULDERS,
    BICEPS,
    TRICEPS,
    FOREARMS,
    QUADS,
    HAMSTRINGS,
    ADDUCTORS,
    CALVES,
    ABS,
    CARDIO,
    FULL_BODY,
    GLUTES,
    OTHER;

    fun toStringResource(): StringResource =
        when (this) {
            CHEST -> Res.string.muscle_group_chest
            BACK -> Res.string.muscle_group_back
            SHOULDERS -> Res.string.muscle_group_shoulders
            BICEPS -> Res.string.muscle_group_biceps
            TRICEPS -> Res.string.muscle_group_triceps
            FOREARMS -> Res.string.muscle_group_forearms
            QUADS -> Res.string.muscle_group_quads
            HAMSTRINGS -> Res.string.muscle_group_hamstrings
            CALVES -> Res.string.muscle_group_calves
            ABS -> Res.string.muscle_group_abs
            CARDIO -> Res.string.muscle_group_cardio
            FULL_BODY -> Res.string.muscle_group_full_body
            GLUTES -> Res.string.muscle_group_glutes
            ADDUCTORS -> Res.string.muscle_group_adductors
            OTHER -> Res.string.muscle_group_other
        }
}