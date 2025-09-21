package org.darthacheron.fitbe.workouts.templates

import androidx.compose.runtime.Composable
//import org.darthacheron.fitbe.database.workoutList // Will be created later
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class WorkoutTemplate(
    val id: Uuid = Uuid.random(), // Default to random for new instances
    val name: String,
    val description: String? = null,
    val imageUri: String? = null, // Added imageUri field
    val default: Boolean = false,
    // This will be populated by joining with WorkoutTemplateExercise and WorkoutTemplateSet
    val exercises: List<WorkoutTemplateExercise> = emptyList()
)

@Composable
internal fun getWorkoutName(name: String, default: Boolean): String {
    return if (default) { // TODO: check workoutList when added
        DefaultWorkoutResProvider.workoutNameMap[name]?.let {
            stringResource(it)
        } ?: name
    } else {
        name
    }
}

@Composable
internal fun getWorkoutImage(imageUri: String?, default: Boolean): DrawableResource? {
    return if (default && imageUri != null && imageUri.startsWith("default_workout_template_")) {
        DefaultWorkoutResProvider.workoutImageMap[imageUri]
    } else {
        null
    }
}
