// package org.darthacheron.fitbe.workouts.templates
//
// import androidx.room.Entity
// import androidx.room.ForeignKey
// import androidx.room.Index
// import androidx.room.PrimaryKey
// import kotlin.uuid.ExperimentalUuidApi
// import kotlin.uuid.Uuid
// import org.darthacheron.fitbe.workouts.exercises.ExerciseEntity
// import org.darthacheron.fitbe.workouts.exercises.toEntity
//
// @OptIn(ExperimentalUuidApi::class)
// @Entity(
//     tableName = "workout_template_exercises",
//     foreignKeys = [
//         ForeignKey(
//             entity = WorkoutTemplateEntity::class,
//             parentColumns = ["id"],
//             childColumns = ["workoutTemplateId"],
//             onDelete = ForeignKey.CASCADE
//         ),
//         ForeignKey(
//             entity = ExerciseEntity::class,
//             parentColumns = ["id"],
//             childColumns = ["exerciseId"],
//             onDelete = ForeignKey.CASCADE // Or RESTRICT if you want to prevent exercise deletion if used in templates
//         )
//     ],
//     indices = [
//         Index(value = ["workoutTemplateId"]),
//         Index(value = ["exerciseId"])
//     ]
// )
// data class WorkoutTemplateExerciseEntity(
//     @PrimaryKey val id: Uuid,
//     val workoutTemplateId: Uuid,
//     val exerciseId: Uuid,
//     val exercise: ExerciseEntity, // TODO: Check if this is correct
//     val exerciseOrder: Int
// ) {
//     fun toWorkoutTemplateExercise(): WorkoutTemplateExercise {
//         return WorkoutTemplateExercise(
//             id = this.id,
//             workoutTemplateId = this.workoutTemplateId,
//             exercise = this.exercise.toExercise(), // TODO: Check if this is correct
//             exerciseOrder = this.exerciseOrder
//             // sets will be loaded separately
//         )
//     }
// }
//
// @OptIn(ExperimentalUuidApi::class)
// fun WorkoutTemplateExercise.toEntity(): WorkoutTemplateExerciseEntity {
//     return WorkoutTemplateExerciseEntity(
//         id = this.id,
//         workoutTemplateId = this.workoutTemplateId,
//         exercise = this.exercise.toEntity(), // TODO: Check if this is correct
//         exerciseId = this.exercise.id,
//         exerciseOrder = this.exerciseOrder
//     )
// }