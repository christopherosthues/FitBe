package org.darthacheron.fitbe.exercises.exercises

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_add_edit_exercise
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.database.equipmentList
import org.darthacheron.fitbe.exercises.equipment.TrainingEquipment // Assuming TrainingEquipment is accessible
// Assuming MuscleGroup is accessible, e.g., defined in this package or imported
// import org.darthacheron.fitbe.shared.MuscleGroup 
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
    val equipmentList: List<TrainingEquipment> = emptyList(),
    val isLoading: Boolean = false,
    val isEditing: Boolean = false, // True if UI should be in edit mode
    val exerciseId: Uuid? = null,
    val error: String? = null,
    val default: Boolean = false,
    val persistedDefaultName: String? = null,
    val persistedDefaultGuide: String? = null,
    val persistedDefaultMuscleGroups: List<MuscleGroup>? = null,
    val persistedDefaultEquipmentList: List<TrainingEquipment>? = null,
    val isModifiedFromPersistedDefault: Boolean = false
)

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

    private val _saveCompletedEvent = MutableSharedFlow<Unit>()
    val saveCompletedEvent = _saveCompletedEvent.asSharedFlow()

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
                    error = null,
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
                        // Note: originalDefaultExercise (type Exercise) does not contain equipment.
                        // For persistedDefaultEquipmentList, we'll use the currentExerciseWithEquipment's list as the initial baseline for a default item.
                        // A more robust solution would involve fetching the original default equipment configuration if it can differ.
                        _uiState.update {
                            it.copy(
                                name = currentExerciseWithEquipment.name,
                                guide = currentExerciseWithEquipment.guide,
                                targetMuscleGroups = currentExerciseWithEquipment.targetMuscleGroups,
                                equipmentList = currentExerciseWithEquipment.equipmentList,
                                default = true,
                                isLoading = false,
                                isEditing = true, // Default to edit mode for existing exercises too
                                exerciseId = currentExerciseWithEquipment.id,
                                error = null,
                                persistedDefaultName = originalDefaultExercise?.name,
                                persistedDefaultGuide = originalDefaultExercise?.guide,
                                persistedDefaultMuscleGroups = originalDefaultExercise?.targetMuscleGroups,
                                persistedDefaultEquipmentList = originalDefaultExercise?.equipmentList, // Baseline for default equipment
                                isModifiedFromPersistedDefault = if (originalDefaultExercise != null) {
                                    (currentExerciseWithEquipment.name != originalDefaultExercise.name ||
                                     currentExerciseWithEquipment.guide != originalDefaultExercise.guide ||
                                     !currentExerciseWithEquipment.targetMuscleGroups.equalsSet(originalDefaultExercise.targetMuscleGroups) ||
                                     !currentExerciseWithEquipment.equipmentList.equalsSet(originalDefaultExercise.equipmentList)) // TODO: Need a robust equalsSet for list comparison if order doesn't matter
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
                                isEditing = true, // Default to edit mode
                                exerciseId = currentExerciseWithEquipment.id,
                                error = null,
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
                        it.copy(isLoading = false, error = "Exercise not found", isEditing = false,
                            name = "", guide = "", targetMuscleGroups = emptyList(), equipmentList = emptyList()
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Failed to load exercise: ${e.message}", isEditing = false,
                        name = "", guide = "", targetMuscleGroups = emptyList(), equipmentList = emptyList()
                    )
                }
            }
        }
    }

    private fun <T> List<T>.equalsSet(other: List<T>?): Boolean {
        if (other == null) return false
        return this.size == other.size && this.toSet() == other.toSet()
    }

    fun onNameChange(name: String) {
        _uiState.update { currentState ->
            val modified = if (currentState.default) {
                (name != currentState.persistedDefaultName ||
                 currentState.guide != currentState.persistedDefaultGuide ||
                 !currentState.targetMuscleGroups.equalsSet(currentState.persistedDefaultMuscleGroups ?: emptyList()) ||
                 !currentState.equipmentList.equalsSet(currentState.persistedDefaultEquipmentList ?: emptyList()))
            } else { false }
            currentState.copy(name = name, error = null, isModifiedFromPersistedDefault = modified)
        }
    }

    fun onGuideChange(guide: String) {
        _uiState.update { currentState ->
            val modified = if (currentState.default) {
                (currentState.name != currentState.persistedDefaultName ||
                 guide != currentState.persistedDefaultGuide ||
                 !currentState.targetMuscleGroups.equalsSet(currentState.persistedDefaultMuscleGroups ?: emptyList()) ||
                 !currentState.equipmentList.equalsSet(currentState.persistedDefaultEquipmentList ?: emptyList()))
            } else { false }
            currentState.copy(guide = guide, error = null, isModifiedFromPersistedDefault = modified)
        }
    }

    fun onTargetMuscleGroupsChange(muscleGroups: List<MuscleGroup>) {
         _uiState.update { currentState ->
            val modified = if (currentState.default) {
                (currentState.name != currentState.persistedDefaultName ||
                 currentState.guide != currentState.persistedDefaultGuide ||
                 !muscleGroups.equalsSet(currentState.persistedDefaultMuscleGroups ?: emptyList()) ||
                 !currentState.equipmentList.equalsSet(currentState.persistedDefaultEquipmentList ?: emptyList()))
            } else { false }
            currentState.copy(targetMuscleGroups = muscleGroups, error = null, isModifiedFromPersistedDefault = modified)
        }
    }

    fun onEquipmentListChange(equipment: List<TrainingEquipment>) {
        _uiState.update { currentState ->
            val modified = if (currentState.default) {
                 (currentState.name != currentState.persistedDefaultName ||
                 currentState.guide != currentState.persistedDefaultGuide ||
                 !currentState.targetMuscleGroups.equalsSet(currentState.persistedDefaultMuscleGroups ?: emptyList()) ||
                 !equipment.equalsSet(currentState.persistedDefaultEquipmentList ?: emptyList()))
            } else { false }
            currentState.copy(equipmentList = equipment, error = null, isModifiedFromPersistedDefault = modified)
        }
    }

    fun saveExercise() {
        val currentState = _uiState.value
        if (currentState.name.isBlank()) {
            _uiState.update { it.copy(error = "Name cannot be empty") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val exerciseToSave = Exercise(
                id = currentState.exerciseId ?: Uuid.random(),
                name = currentState.name,
                guide = currentState.guide,
                targetMuscleGroups = currentState.targetMuscleGroups,
                default = currentState.default,
                dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
            )

            try {
                // IMPORTANT: Persisting equipmentList with the exercise is not directly handled by
                // exerciseRepository.upsertExercise(Exercise). This linkage needs to be handled
                // in the repository/DAO layer, possibly by a separate method or by modifying Exercise entity/DAO.
                exerciseRepository.upsertExercise(exerciseToSave)
                // Example: exerciseRepository.updateExerciseEquipmentLinks(exerciseToSave.id, currentState.equipmentList.map { it.id })

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEditing = false,
                        exerciseId = exerciseToSave.id,
                        name = exerciseToSave.name,
                        guide = exerciseToSave.guide,
                        targetMuscleGroups = exerciseToSave.targetMuscleGroups,
                        // equipmentList remains as is in UI state, or could be re-fetched
                        error = null,
                    )
                }
                _saveCompletedEvent.emit(Unit)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to save exercise: ${e.message}") }
            }
        }
    }

    fun resetExerciseToDefault() {
        val currentState = _uiState.value
        if (currentState.exerciseId == null || !currentState.default) {
            _uiState.update { it.copy(error = "Cannot reset non-default exercise or new exercise.") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                exerciseRepository.resetExerciseToDefault(currentState.exerciseId)
                loadExercise(currentState.exerciseId.toString()) // Reload to reflect reset state
                _saveCompletedEvent.emit(Unit) 
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to reset exercise: ${e.message}") }
            }
        }
    }

    fun deleteExercise() {
        val currentState = _uiState.value
        if (currentState.exerciseId == null || currentState.default) {
            _uiState.update { it.copy(error = "Cannot delete default exercise or new exercise.") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val exerciseToDelete = Exercise( // Construct based on current state to pass to repository
                    id = currentState.exerciseId,
                    name = currentState.name,
                    guide = currentState.guide,
                    targetMuscleGroups = currentState.targetMuscleGroups,
                    default = currentState.default,
                    dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date 
                )
                exerciseRepository.deleteExercise(exerciseToDelete)
                navHostController.popBackStack()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to delete exercise: ${e.message}") }
            } finally {
                 _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
