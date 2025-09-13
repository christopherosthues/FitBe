package org.darthacheron.fitbe.workouts.templates

import androidx.compose.runtime.Composable
import org.darthacheron.fitbe.database.exerciseList
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class WorkoutTemplate(
    val id: Uuid = Uuid.random(), // Default to random for new instances
    val name: String,
    val description: String? = null,
    // This will be populated by joining with WorkoutTemplateExercise and WorkoutTemplateSet
    val exercises: List<WorkoutTemplateExercise> = emptyList()
)

// TODO:
//@Composable
//internal fun getWorkoutName(name: String, default: Boolean): String {
//    return if (default && workoutList.any { it.key == name }) {
//        DefaultWorkoutResProvider.workoutNameMap[name]?.let {
//            stringResource(it)
//        } ?: name
//    } else {
//        name
//    }
//}