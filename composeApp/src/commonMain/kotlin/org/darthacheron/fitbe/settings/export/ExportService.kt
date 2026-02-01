package org.darthacheron.fitbe.settings.export

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.writeString
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import org.darthacheron.fitbe.health.beverages.BeverageRepository
import org.darthacheron.fitbe.health.sleep.SleepRepository
import org.darthacheron.fitbe.health.steps.StepsRepository
import org.darthacheron.fitbe.health.weight.BodyWeightRepository
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.workouts.equipment.EquipmentRepository
import org.darthacheron.fitbe.workouts.exercises.ExerciseRepository
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class ExportService(
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    private val beverageRepository: BeverageRepository,
    private val stepsRepository: StepsRepository,
    private val sleepRepository: SleepRepository,
    private val bodyWeightRepository: BodyWeightRepository,
    private val exerciseRepository: ExerciseRepository,
    private val equipmentRepository: EquipmentRepository,
) {
    suspend fun export(exportState: ExportDialogUiState) {
        val settings = settingsRepository.getSettings()
        val profileId = settings.selectedProfileId ?: return
        val profile = profileRepository.getProfileById(profileId) ?: return

        // TODO: Not all properties of the profile are exported yet
        val dataToExport = FitBeExportData(
            appVersion = "1.0.0",//APP_VERSION,
            profile = profile,
            beverages = if (exportState.exportBeverages) {
                beverageRepository.getAllBeveragesForProfile(profileId)
            } else {
                emptyList()
            },

            steps = if (exportState.exportSteps) {
                stepsRepository.getAllStepsForProfile(profileId)
            } else {
                emptyList()
            },

            sleep = if (exportState.exportSleep) {
                sleepRepository.getAllSleepsForProfile(profileId)
            } else {
                emptyList()
            },

            bodyWeights = if (exportState.exportWeight) {
                bodyWeightRepository.getAllBodyWeightsForProfile(profileId)
            } else {
                emptyList()
            },

            exercises = if (exportState.exportExercises) {
                if (exportState.exportExercisesIncludeDefaults) {
                    exerciseRepository.getAllExercisesWithEquipment().map { it.toExportExercise() }
                } else {
                    exerciseRepository.getAllUserExercisesWithEquipment().map { it.toExportExercise() }
                }
            } else {
                emptyList()
            },

            equipments = if (exportState.exportEquipment) {
                if (exportState.exportEquipmentIncludeDefaults) {
                    equipmentRepository.getAllEquipmentWithExercises().map { it.toExportEquipment() }
                } else {
                    equipmentRepository.getAllUserEquipmentWithExercises().map { it.toExportEquipment() }
                }
            } else {
                emptyList()
            },

            favoriteExerciseIds = if (exportState.exportExercises && exportState.exportFavoriteExercises) {
                exerciseRepository.getFavoriteExerciseIds(profileId).first()
            } else {
                emptyList()
            },

            favoriteEquipmentIds = if (exportState.exportEquipment && exportState.exportFavoriteEquipment) {
                equipmentRepository.getFavoriteEquipmentIds(profileId).first()
            } else {
                emptyList()
            }
        )

        // Configure the Json encoder for pretty printing
        val json = Json { prettyPrint = true }

        // Encode the data object into a JSON string
        val jsonString = json.encodeToString(FitBeExportData.serializer(), dataToExport)

        val platformFile = PlatformFile(exportState.exportPath)
        platformFile.writeString(jsonString)
    }
}
