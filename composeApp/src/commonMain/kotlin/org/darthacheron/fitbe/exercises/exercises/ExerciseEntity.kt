package org.darthacheron.fitbe.exercises.exercises

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters // Added
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.database.converters.MuscleGroupListConverter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "exercises", indices = [Index(value = ["name"], unique = true)])
@TypeConverters(MuscleGroupListConverter::class) // Added
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid = Uuid.random(),
    @ColumnInfo(name = "name") val name: String,
    val guide: String,
    val targetMuscleGroups: List<MuscleGroup> = emptyList(), // Added
    val imageUri: String? = null,
    val default: Boolean = false,
    val dateUtc: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
) {
    fun toExercise(): Exercise {
        return Exercise(
            id = id,
            name = name,
            guide = guide,
            targetMuscleGroups = targetMuscleGroups,
            imageUri = imageUri,
            default = default,
            dateUtc = dateUtc
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun toEntity(exercise: Exercise): ExerciseEntity {
    return ExerciseEntity(
        id = exercise.id,
        name = exercise.name,
        guide = exercise.guide,
        targetMuscleGroups = exercise.targetMuscleGroups,
        imageUri = exercise.imageUri,
        default = exercise.default,
        dateUtc = exercise.dateUtc
    )
}
