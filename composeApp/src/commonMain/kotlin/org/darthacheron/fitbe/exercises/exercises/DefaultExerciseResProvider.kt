package org.darthacheron.fitbe.exercises.exercises

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.default_training_equipment_spotter_arms
import org.jetbrains.compose.resources.StringResource

object DefaultExerciseResProvider {
    val exerciseNameMap: Map<String, StringResource> = mapOf(
        "default_training_equipment_spotter_arms" to Res.string.default_training_equipment_spotter_arms
    )
}