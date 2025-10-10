// package org.darthacheron.fitbe.workouts.templates
//
// import androidx.room.Embedded
// import androidx.room.Relation
// import kotlin.uuid.ExperimentalUuidApi
//
// /**
//  * Data class to hold a WorkoutTemplateExerciseEntity and its related WorkoutTemplateSetEntity objects.
//  */
// @OptIn(ExperimentalUuidApi::class)
// data class WorkoutTemplateExerciseWithSets(
//     @Embedded
//     val exercise: WorkoutTemplateExerciseEntity,
//
//     @Relation(
//         parentColumn = "id", // Primary key of WorkoutTemplateExerciseEntity
//         entityColumn = "workoutTemplateExerciseId" // Foreign key in WorkoutTemplateSetEntity
//     )
//     val sets: List<WorkoutTemplateSetEntity>
// ) {
//     fun toWorkoutTemplateExercise(): WorkoutTemplateExercise {
//         return exercise.toWorkoutTemplateExercise().copy(
//             sets = sets.map { it.toWorkoutTemplateSet() }
//         )
//     }
// }
