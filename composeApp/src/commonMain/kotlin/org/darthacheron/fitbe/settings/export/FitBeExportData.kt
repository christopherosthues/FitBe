@file:OptIn(ExperimentalUuidApi::class)

package org.darthacheron.fitbe.settings.export

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.darthacheron.fitbe.health.beverages.Beverage
import org.darthacheron.fitbe.health.sleep.Sleep
import org.darthacheron.fitbe.health.steps.Steps
import org.darthacheron.fitbe.health.weight.BodyWeight
import org.darthacheron.fitbe.profile.Profile
import org.darthacheron.fitbe.workouts.equipment.EquipmentWithExercises
import org.darthacheron.fitbe.workouts.exercises.ExerciseType
import org.darthacheron.fitbe.workouts.exercises.ExerciseWithEquipment
import org.darthacheron.fitbe.workouts.exercises.MuscleGroup
import org.darthacheron.fitbe.workouts.exercises.RecommendedFor
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class FitBeExportData(
    val profile: Profile,
    val beverages: List<Beverage> = emptyList(),
    val steps: List<Steps> = emptyList(),
    val sleep: List<Sleep> = emptyList(),
    val bodyWeights: List<BodyWeight> = emptyList(),
    val exercises: List<ExportExercise> = emptyList(),
    val equipments: List<ExportEquipment> = emptyList()
)

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class ExportExercise(
    val id: Uuid,
    val name: String,
    val guide: String,
    val targetMuscleGroups: List<MuscleGroup> = emptyList(),
    val imageUri: String?,
    val default: Boolean = false,
    val recommendedFor: List<RecommendedFor> = emptyList(),
    val exerciseType: ExerciseType,
    val dateUtc: LocalDate,
    val equipmentIds: List<Uuid>
)

@OptIn(ExperimentalUuidApi::class)
fun ExerciseWithEquipment.toExportExercise(): ExportExercise {
    return ExportExercise(
        id = this.id,
        name = this.name,
        guide = this.guide,
        targetMuscleGroups = this.targetMuscleGroups,
        imageUri = this.imageUri,
        default = this.default,
        recommendedFor = this.recommendedFor,
        exerciseType = this.exerciseType,
        dateUtc = this.dateUtc,
        equipmentIds = this.equipmentList.map { it.id }
    )
}

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class ExportEquipment(
    val id: Uuid,
    val name: String,
    val default: Boolean = false,
    val imageUri: String? = null,
    val dateUtc: LocalDate,
    val exerciseIds: List<Uuid>
)

fun EquipmentWithExercises.toExportEquipment(): ExportEquipment {
    return ExportEquipment(
        id = this.id,
        name = this.name,
        default = this.default,
        imageUri = this.imageUri,
        dateUtc = this.dateUtc,
        exerciseIds = this.exercises.map { it.id }
    )
}