package org.darthacheron.fitbe.workouts.exercises

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "default_exercises", indices = [Index(value = ["name"], unique = true)])
data class DefaultExerciseEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid,
    @ColumnInfo(name = "name") val name: String,
    val guide: String,
    val targetMuscleGroups: List<MuscleGroup> = emptyList(),
    val imageUri: String? = null,
    val recommendedFor: List<RecommendedFor>,
    val exerciseType: ExerciseType,
    val dateUtc: LocalDate =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.UTC)
            .date
) {
    fun toExerciseEntity(): ExerciseEntity =
        ExerciseEntity(
            id = id,
            name = name,
            guide = guide,
            targetMuscleGroups = targetMuscleGroups,
            imageUri = imageUri,
            default = true,
            recommendedFor = recommendedFor,
            exerciseType = exerciseType,
            dateUtc = dateUtc
        )

    fun toExercise(): Exercise =
        Exercise(
            id = id,
            name = name,
            guide = guide,
            targetMuscleGroups = targetMuscleGroups,
            imageUri = imageUri,
            default = true,
            recommendedFor = recommendedFor,
            exerciseType = exerciseType,
            dateUtc = dateUtc
        )
}

@OptIn(ExperimentalUuidApi::class)
fun ExerciseEntity.toDefaultExerciseEntity(): DefaultExerciseEntity =
    DefaultExerciseEntity(
        id = this.id,
        name = this.name,
        guide = this.guide,
        targetMuscleGroups = this.targetMuscleGroups,
        imageUri = this.imageUri,
        recommendedFor = this.recommendedFor,
        exerciseType = this.exerciseType,
        dateUtc = this.dateUtc
    )