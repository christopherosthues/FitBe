package org.darthacheron.fitbe.settings.import

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import kotlinx.serialization.json.Json
import org.darthacheron.fitbe.health.beverages.BeverageRepository
import org.darthacheron.fitbe.health.sleep.SleepRepository
import org.darthacheron.fitbe.health.steps.StepsRepository
import org.darthacheron.fitbe.health.weight.BodyWeightRepository
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.export.FitBeExportData
import org.darthacheron.fitbe.settings.export.toExercise
import org.darthacheron.fitbe.settings.export.toTrainingEquipment
import org.darthacheron.fitbe.workouts.equipment.EquipmentRepository
import org.darthacheron.fitbe.workouts.exercises.ExerciseRepository
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
class ImportService(
    private val profileRepository: ProfileRepository,
    private val beverageRepository: BeverageRepository,
    private val stepsRepository: StepsRepository,
    private val sleepRepository: SleepRepository,
    private val bodyWeightRepository: BodyWeightRepository,
    private val exerciseRepository: ExerciseRepository,
    private val equipmentRepository: EquipmentRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend fun import(importState: ImportDialogUiState) {
        val jsonString = PlatformFile(importState.importPath).readString()
        val importData = Json.decodeFromString<FitBeExportData>(jsonString)


        val profileId = if (importState.importProfile) {
            val importProfileId = if (profileRepository.getProfileById(importData.profile.id) != null) {
                importData.profile.id
            } else {
                Uuid.random()
            }
            val newProfile = importData.profile.copy(id = importProfileId)
            profileRepository.upsertProfile(newProfile)
            newProfile.id
        } else {
            importState.selectedProfileId ?: return
        }

        // TODO: validation for duplicated entries
        beverageRepository.upsertAll(importData.beverages.map { it.copy(profileId = profileId) })
        stepsRepository.upsertAll(importData.steps.map { it.copy(profileId = profileId) })
        sleepRepository.upsertAll(importData.sleep.map { it.copy(profileId = profileId) })
        bodyWeightRepository.upsertAll(importData.bodyWeights.map { it.copy(profileId = profileId) })

        // TODO: Check if AI has done something right
        importData.exercises.forEach { exercise ->
            exerciseRepository.upsertExercise(exercise.toExercise())
            exerciseRepository.updateExerciseEquipmentLinks(exercise.id, exercise.equipmentIds)
        }

        importData.equipments.forEach { equipment ->
            equipmentRepository.upsertEquipment(equipment.toTrainingEquipment())
        }

        importData.favoriteExerciseIds.forEach { exerciseId ->
            exerciseRepository.addFavorite(profileId, exerciseId)
        }

        importData.favoriteEquipmentIds.forEach { equipmentId ->
            equipmentRepository.addFavorite(profileId, equipmentId)
        }
    }
}
