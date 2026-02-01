package org.darthacheron.fitbe.workouts.exercises

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Entity(tableName = "exercises", indices = [Index(value = ["name"], unique = true)])
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid = Uuid.random(),
    @ColumnInfo(name = "name") val name: String,
    val guide: String,
    val targetMuscleGroups: List<MuscleGroup> = emptyList(),
    val imageUri: String? = null,
    val default: Boolean = false,
    val recommendedFor: List<RecommendedFor>,
    val exerciseType: ExerciseType,
    val dateUtc: LocalDate =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.UTC)
            .date,
    val lastModified: Instant?
) {
    fun toExercise(): Exercise =
        Exercise(
            id = id,
            name = name,
            guide = guide,
            targetMuscleGroups = targetMuscleGroups,
            imageUri = imageUri,
            default = default,
            recommendedFor = recommendedFor,
            exerciseType = exerciseType,
            dateUtc = dateUtc,
            lastModified = lastModified
        )
}

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
fun Exercise.toExerciseEntity(): ExerciseEntity =
    ExerciseEntity(
        id = this.id,
        name = this.name,
        guide = this.guide,
        targetMuscleGroups = this.targetMuscleGroups,
        imageUri = this.imageUri,
        default = this.default,
        recommendedFor = this.recommendedFor,
        exerciseType = this.exerciseType,
        dateUtc = this.dateUtc,
        lastModified = this.lastModified
    )