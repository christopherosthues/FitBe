package org.darthacheron.fitbe.workouts.templates

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class WorkoutTemplate(
    val id: Uuid = Uuid.random(),
    val name: String,
    val description: String? = null,
    val imageUri: String? = null,
    val default: Boolean = false,
    val lastModified: Instant,
    // This will be populated by joining with WorkoutTemplateExercise and WorkoutTemplateSet
    val exercises: List<WorkoutTemplateExercise> = emptyList()
)

@Composable
internal fun getWorkoutName(
    name: String,
    default: Boolean
): String =
    if (default) { // TODO: check workoutList when added
        DefaultWorkoutResProvider.workoutNameMap[name]?.let {
            stringResource(it)
        } ?: name
    } else {
        name
    }

@Composable
internal fun getWorkoutImage(
    imageUri: String?,
    default: Boolean
): DrawableResource? =
    if (default && imageUri != null && imageUri.startsWith("default_workout_template_")) {
        DefaultWorkoutResProvider.workoutImageMap[imageUri]
    } else {
        null
    }