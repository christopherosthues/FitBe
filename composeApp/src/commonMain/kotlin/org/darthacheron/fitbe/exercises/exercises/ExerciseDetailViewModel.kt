package org.darthacheron.fitbe.exercises.exercises

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.exercise_detail_error_delete_default_exercise
import fitbe.composeapp.generated.resources.exercise_detail_error_delete_exercise
import fitbe.composeapp.generated.resources.exercise_detail_error_delete_new_exercise
import fitbe.composeapp.generated.resources.exercise_detail_error_exercise_not_found
import fitbe.composeapp.generated.resources.exercise_detail_error_loading_exercise
import fitbe.composeapp.generated.resources.exercise_detail_error_missing_guide
import fitbe.composeapp.generated.resources.exercise_detail_error_missing_muscle_group
import fitbe.composeapp.generated.resources.exercise_detail_error_missing_name
import fitbe.composeapp.generated.resources.exercise_detail_error_reset_default_exercise
import fitbe.composeapp.generated.resources.exercise_detail_error_reset_new_exercise
import fitbe.composeapp.generated.resources.exercise_detail_error_reset_non_default_exercise
import fitbe.composeapp.generated.resources.exercise_detail_error_saving_exercise
import fitbe.composeapp.generated.resources.top_bar_title_add_edit_exercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.exercises.equipment.TrainingEquipment
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class AddEditExerciseUiState(
    val name: String = "",
    val guide: String = "",
    val targetMuscleGroups: List<MuscleGroup> = emptyList(),
    val imageUri: String? = null,
    val equipmentList: List<TrainingEquipment> = emptyList(),
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val exerciseId: Uuid? = null,
    val error: ExerciseError = ExerciseError(),
    val default: Boolean = false,
    val persistedDefaultName: String? = null,
    val persistedDefaultGuide: String? = null,
    val persistedDefaultImageUri: String? = null,
    val persistedDefaultMuscleGroups: List<MuscleGroup>? = null,
    val persistedDefaultEquipmentList: List<TrainingEquipment>? = null,
    val isModifiedFromPersistedDefault: Boolean = false
)

data class ExerciseError(
    val hasGeneralError: Boolean = false,
    val generalError: StringResource? = null,
    val hasNameError: Boolean = false,
    val nameError: StringResource? = null,
    val hasGuideError: Boolean = false,
    val guideError: StringResource? = null,
    val hasMuscleGroupError: Boolean = false,
    val muscleGroupError: StringResource? = null,
) {
    val hasError: Boolean
        get() = hasGeneralError || hasNameError || hasGuideError || hasMuscleGroupError
}

@OptIn(ExperimentalUuidApi::class)
class ExerciseDetailViewModel(
    private val exerciseRepository: ExerciseRepository,
    private val navHostController: NavHostController,
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_add_edit_exercise

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard

    private val _uiState = MutableStateFlow(AddEditExerciseUiState())
    val uiState: StateFlow<AddEditExerciseUiState> = _uiState.asStateFlow()

    val availableMuscleGroups: StateFlow<List<MuscleGroup>> = _uiState
        .map { currentState ->
            MuscleGroup.entries.filter { it !in currentState.targetMuscleGroups }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, MuscleGroup.entries)

    private fun <T> List<T>.idsEqual(other: List<T>, idSelector: (T) -> Any): Boolean {
        if (this.size != other.size) return false
        return this.map(idSelector).toSet() == other.map(idSelector).toSet()
    }

    fun loadExercise(exerciseIdString: String?) {
        if (exerciseIdString == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isEditing = true, // New exercise starts in edit mode
                    default = false,
                    exerciseId = null,
                    name = "",
                    guide = "",
                    targetMuscleGroups = emptyList(),
                    equipmentList = emptyList(),
                    error = ExerciseError(),
                    persistedDefaultName = null,
                    persistedDefaultGuide = null,
                    persistedDefaultMuscleGroups = null,
                    persistedDefaultEquipmentList = null,
                    isModifiedFromPersistedDefault = false
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val parsedExerciseId = Uuid.parse(exerciseIdString)
                val currentExerciseWithEquipment = exerciseRepository.getExerciseWithExercisesById(parsedExerciseId).firstOrNull()

                if (currentExerciseWithEquipment != null) {
                    if (currentExerciseWithEquipment.default) {
                        val originalDefaultExercise = exerciseRepository.getDefaultExerciseWithEquipment(currentExerciseWithEquipment.id).firstOrNull()
                        _uiState.update {
                            it.copy(
                                name = currentExerciseWithEquipment.name,
                                guide = currentExerciseWithEquipment.guide,
                                targetMuscleGroups = currentExerciseWithEquipment.targetMuscleGroups,
                                equipmentList = currentExerciseWithEquipment.equipmentList,
                                default = true,
                                isLoading = false,
                                isEditing = false,
                                exerciseId = currentExerciseWithEquipment.id,
                                error = ExerciseError(),
                                persistedDefaultName = originalDefaultExercise?.name,
                                persistedDefaultGuide = originalDefaultExercise?.guide,
                                persistedDefaultMuscleGroups = originalDefaultExercise?.targetMuscleGroups,
                                persistedDefaultEquipmentList = originalDefaultExercise?.equipmentList,
                                isModifiedFromPersistedDefault = if (originalDefaultExercise != null) {
                                    (currentExerciseWithEquipment.name != originalDefaultExercise.name ||
                                     currentExerciseWithEquipment.guide != originalDefaultExercise.guide ||
                                     !currentExerciseWithEquipment.targetMuscleGroups.idsEqual(originalDefaultExercise.targetMuscleGroups) { mg -> mg.ordinal } ||
                                     !currentExerciseWithEquipment.equipmentList.idsEqual(originalDefaultExercise.equipmentList) { eq -> eq.id })
                                } else { false } 
                            )
                        }
                    } else { // Not a default item
                        _uiState.update {
                            it.copy(
                                name = currentExerciseWithEquipment.name,
                                guide = currentExerciseWithEquipment.guide,
                                targetMuscleGroups = currentExerciseWithEquipment.targetMuscleGroups,
                                equipmentList = currentExerciseWithEquipment.equipmentList,
                                default = false,
                                isLoading = false,
                                isEditing = false,
                                exerciseId = currentExerciseWithEquipment.id,
                                error = ExerciseError(),
                                persistedDefaultName = null,
                                persistedDefaultGuide = null,
                                persistedDefaultMuscleGroups = null,
                                persistedDefaultEquipmentList = null,
                                isModifiedFromPersistedDefault = false
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = ExerciseError(
                                hasGeneralError = true,
                                generalError = Res.string.exercise_detail_error_exercise_not_found
                            ),
                            isEditing = false,
                            name = "",
                            guide = "",
                            targetMuscleGroups = emptyList(),
                            equipmentList = emptyList()
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = ExerciseError(
                            hasGeneralError = true,
                            generalError = Res.string.exercise_detail_error_loading_exercise
                        ),
                        isEditing = false,
                        name = "",
                        guide = "",
                        targetMuscleGroups = emptyList(),
                        equipmentList = emptyList()
                    )
                }
            }
        }
    }

    fun setIsEditing(isEditing: Boolean) {
        _uiState.update { it.copy(isEditing = isEditing) }
    }

    fun onImageUriChange(imageUri: String?) {
        _uiState.update { currentState ->
            val modified = if (currentState.default && currentState.persistedDefaultName != null) {
                (currentState.name != currentState.persistedDefaultName || imageUri != currentState.persistedDefaultImageUri)
            } else {
                false
            }
            currentState.copy(
                imageUri = imageUri,
                error = currentState.error.copy(hasGeneralError = false, generalError = null),
                isModifiedFromPersistedDefault = modified
            )
        }
    }

    fun onNameChange(name: String) {
        val nameError =
            if (name.isBlank()) Res.string.exercise_detail_error_missing_name else null
        // TODO: check if exercise with same name already exists
        // set hasNameError = true and nameError = Res.string.exercise_detail_error_name_already_exists

        _uiState.update { currentState ->
            val modified = if (currentState.default) {
                (name != currentState.persistedDefaultName ||
                 currentState.guide != currentState.persistedDefaultGuide ||
                 !currentState.targetMuscleGroups.idsEqual(currentState.persistedDefaultMuscleGroups ?: emptyList()) { mg -> mg.ordinal } ||
                 !currentState.equipmentList.idsEqual(currentState.persistedDefaultEquipmentList ?: emptyList()) { eq -> eq.id })
            } else { false }
            currentState.copy(
                name = name,
                error = currentState.error.copy(
                    hasNameError = nameError != null,
                    nameError = nameError,
                    hasGeneralError = false,
                    generalError = null
                ),
                isModifiedFromPersistedDefault = modified
            )
        }
    }

    fun onGuideChange(guide: String) {
        val guideError =
            if (guide.isBlank()) Res.string.exercise_detail_error_missing_guide else null

        _uiState.update { currentState ->
            val modified = if (currentState.default) {
                (currentState.name != currentState.persistedDefaultName ||
                 guide != currentState.persistedDefaultGuide ||
                 !currentState.targetMuscleGroups.idsEqual(currentState.persistedDefaultMuscleGroups ?: emptyList()) { mg -> mg.ordinal } ||
                 !currentState.equipmentList.idsEqual(currentState.persistedDefaultEquipmentList ?: emptyList()) { eq -> eq.id })
            } else { false }
            currentState.copy(
                guide = guide,
                error = currentState.error.copy(
                    hasGuideError = guideError != null,
                    guideError = guideError,
                    hasGeneralError = false,
                    generalError = null
                ),
                isModifiedFromPersistedDefault = modified
            )
        }
    }

    private fun onTargetMuscleGroupsChange(muscleGroups: List<MuscleGroup>) {
        val muscleGroupError =
            if (muscleGroups.isEmpty()) Res.string.exercise_detail_error_missing_muscle_group else null

         _uiState.update { currentState ->
            val modified = if (currentState.default) {
                (currentState.name != currentState.persistedDefaultName ||
                 currentState.guide != currentState.persistedDefaultGuide ||
                 !muscleGroups.idsEqual(currentState.persistedDefaultMuscleGroups ?: emptyList()) { mg -> mg.ordinal } ||
                 !currentState.equipmentList.idsEqual(currentState.persistedDefaultEquipmentList ?: emptyList()) { eq -> eq.id })
            } else { false }
             currentState.copy(
                 targetMuscleGroups = muscleGroups,
                 error = currentState.error.copy(
                     hasMuscleGroupError = muscleGroupError != null,
                     muscleGroupError = muscleGroupError,
                     hasGeneralError = false,
                     generalError = null
                 ),
                 isModifiedFromPersistedDefault = modified
             )
        }
    }

    fun addMuscleGroup(muscleGroup: MuscleGroup) {
        val currentGroups = _uiState.value.targetMuscleGroups
        if (muscleGroup !in currentGroups) {
            onTargetMuscleGroupsChange(currentGroups + muscleGroup)
        }
    }

    fun removeMuscleGroup(muscleGroup: MuscleGroup) {
        val currentGroups = _uiState.value.targetMuscleGroups
        onTargetMuscleGroupsChange(currentGroups - muscleGroup)
    }

    fun onEquipmentListChange(equipment: List<TrainingEquipment>) {
        _uiState.update { currentState ->
            val modified = if (currentState.default) {
                 (currentState.name != currentState.persistedDefaultName ||
                 currentState.guide != currentState.persistedDefaultGuide ||
                 !currentState.targetMuscleGroups.idsEqual(currentState.persistedDefaultMuscleGroups ?: emptyList()) { mg -> mg.ordinal } ||
                 !equipment.idsEqual(currentState.persistedDefaultEquipmentList ?: emptyList()) { eq -> eq.id })
            } else { false }
            currentState.copy(
                equipmentList = equipment,
                error = currentState.error.copy(
                    hasGeneralError = false,
                    generalError = null
                ),
                isModifiedFromPersistedDefault = modified)
        }
    }

    fun saveExercise() {
        val currentState = _uiState.value
        if (currentState.error.hasError) {
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val exerciseIdToUse = currentState.exerciseId ?: Uuid.random()
            val exerciseToSave = Exercise(
                id = exerciseIdToUse,
                name = currentState.name,
                guide = currentState.guide,
                targetMuscleGroups = currentState.targetMuscleGroups,
                default = currentState.default,
                dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
            )

            try {
                exerciseRepository.upsertExercise(exerciseToSave)
                val equipmentIds = currentState.equipmentList.map { it.id }
                exerciseRepository.updateExerciseEquipmentLinks(exerciseIdToUse, equipmentIds)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEditing = false, 
                        exerciseId = exerciseToSave.id,
                        name = exerciseToSave.name,
                        guide = exerciseToSave.guide,
                        targetMuscleGroups = exerciseToSave.targetMuscleGroups,
                        error = ExerciseError(),
                        isModifiedFromPersistedDefault = false 
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = it.error.copy(
                            hasGeneralError = true,
                            generalError = Res.string.exercise_detail_error_saving_exercise
                        )
                    )
                }
            }
        }
    }

    fun resetExerciseToDefault() {
        val currentState = _uiState.value
        val errorRes = when {
            currentState.exerciseId == null -> Res.string.exercise_detail_error_reset_new_exercise
            !currentState.default -> Res.string.exercise_detail_error_reset_non_default_exercise
            else -> null // No error, proceed with reset
        }

        if (errorRes != null) {
            _uiState.update {
                it.copy(
                    error = ExerciseError(
                        hasGeneralError = true,
                        generalError = errorRes
                    )
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                currentState.exerciseId?.let {
                    exerciseRepository.resetExerciseToDefault(currentState.exerciseId)
                    loadExercise(currentState.exerciseId.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = it.error.copy(
                            hasGeneralError = true,
                            generalError = Res.string.exercise_detail_error_reset_default_exercise
                        )
                    )
                }
            }
        }
    }

    fun deleteExercise() {
        val currentState = _uiState.value
        val errorRes = when {
            currentState.exerciseId == null -> Res.string.exercise_detail_error_delete_new_exercise
            currentState.default -> Res.string.exercise_detail_error_delete_default_exercise
            else -> null // No error, proceed with delete
        }

        if (errorRes != null) {
            _uiState.update {
                it.copy(
                    error = ExerciseError(
                        hasGeneralError = true,
                        generalError = errorRes
                    )
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                currentState.exerciseId?.let { exerciseId ->
                    val exerciseToDelete = Exercise(
                        id = exerciseId,
                        name = currentState . name,
                        guide = currentState.guide,
                        targetMuscleGroups = currentState.targetMuscleGroups,
                        default = currentState.default,
                        dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                    )
                    exerciseRepository.deleteExercise(exerciseToDelete)
                    navHostController.popBackStack()
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = it.error.copy(
                            hasGeneralError = true,
                            generalError = Res.string.exercise_detail_error_delete_exercise
                        )
                    )
                }
            } finally {
                 _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
