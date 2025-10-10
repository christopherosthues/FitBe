// package org.darthacheron.fitbe.workouts.templates
//
// import androidx.room.Embedded
// import androidx.room.Relation
// import kotlin.uuid.ExperimentalUuidApi
//
// @OptIn(ExperimentalUuidApi::class)
// data class WorkoutTemplateWithExercisesEntity(
//     @Embedded
//     val workoutTemplate: WorkoutTemplateEntity,
//
//     @Relation(
//         parentColumn = "id", // Primary key of WorkoutTemplateEntity
//         entityColumn = "workoutTemplateId", // Foreign key in WorkoutTemplateExerciseEntity
//         entity = WorkoutTemplateExerciseEntity::class
//     )
//     val exercises: List<WorkoutTemplateExerciseEntity>
// ) {
//     fun toWorkoutTemplate(): WorkoutTemplate {
//         return workoutTemplate.toWorkoutTemplate().copy(
//             exercises = exercises.map { it.toWorkoutTemplateExercise() }
//         )
//     }
// }