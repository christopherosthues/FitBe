// package org.darthacheron.fitbe.workouts.templates
//
// import androidx.room.Embedded
// import androidx.room.Relation
// import kotlin.uuid.ExperimentalUuidApi
//
// /**
//  * Data class to hold a WorkoutTemplateEntity and its related WorkoutTemplateExerciseWithSets objects.
//  */
// @OptIn(ExperimentalUuidApi::class)
// data class WorkoutTemplateWithExercisesAndSets(
//     @Embedded
//     val template: WorkoutTemplateEntity,
//
//     @Relation(
//         entity = WorkoutTemplateExerciseEntity::class, // Intermediate entity
//         parentColumn = "id", // Primary key of WorkoutTemplateEntity
//         entityColumn = "workoutTemplateId" // Foreign key in WorkoutTemplateExerciseEntity
//     )
//     val exercisesWithSets: List<WorkoutTemplateExerciseWithSets> // List of the intermediate relationship class
// ) {
//     fun toWorkoutTemplate(): WorkoutTemplate {
//         return template.toWorkoutTemplate().copy(
//             exercises = exercisesWithSets.map { it.toWorkoutTemplateExercise() }
//         )
//     }
// }