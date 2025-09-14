package org.darthacheron.fitbe.workouts.templates

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_workout_template_detail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.ui.state.TopBarAction
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class WorkoutTemplateDetailUiState(
    val templateId: Uuid? = null,
    val name: String = "",
    val description: String? = null,
    val imageUri: String? = null,
    val exercises: List<WorkoutTemplateExerciseWithDetails> = emptyList(),
    val default: Boolean = false,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val error: StringResource? = null,
    // TODO: Add persistedDefault values if reset functionality is needed for templates
    // val persistedDefaultName: String? = null,
    // val persistedDefaultDescription: String? = null,
    // val persistedDefaultImageUri: String? = null,
    // val isModifiedFromPersistedDefault: Boolean = false
)

@OptIn(ExperimentalUuidApi::class)
data class WorkoutTemplateExerciseWithDetails(
    val templateExerciseId: Uuid,
    val exerciseId: Uuid,
    val exerciseName: String,
    val exerciseImageUri: String?,
    val exerciseDefault: Boolean,
    val exerciseOrder: Int,
    val notes: String? = null,
    val sets: List<WorkoutTemplateSet> = emptyList() // Changed to WorkoutTemplateSet
)

@OptIn(ExperimentalUuidApi::class)
class WorkoutTemplateDetailViewModel(
    private val workoutTemplateRepository: WorkoutTemplateRepository,
    private val navHostController: NavHostController, // For navigation
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_workout_template_detail

    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard

    override val backNavigationIconVisible: Boolean?
        get() = true

    private val _uiState = MutableStateFlow(WorkoutTemplateDetailUiState())
    val uiState: StateFlow<WorkoutTemplateDetailUiState> = _uiState.asStateFlow()

    // TODO: Implement TopBarActions for Edit, Save, Cancel, Delete, Reset (if applicable)
    override val actions: List<TopBarAction>
        get() = emptyList() // Placeholder for now

    fun loadWorkoutTemplate(templateId: Uuid?) {
        if (templateId == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isEditing = true, // New templates start in edit mode
                    templateId = null,
                    name = "",
                    description = null,
                    imageUri = null,
                    exercises = emptyList(),
                    default = false,
                    error = null
                    // TODO: Initialize persistedDefault values if reset functionality is added
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, templateId = templateId, error = null) }
        viewModelScope.launch {
            try {
                val templateWithDetails = workoutTemplateRepository.getWorkoutTemplateWithExercisesAndSets(templateId).firstOrNull()

                if (templateWithDetails != null) {
//                    val detailedExercises = templateWithDetails.exercises.map { exerciseWithSets ->
//                        WorkoutTemplateExerciseWithDetails(
//                            templateExerciseId = exerciseWithSets.workoutTemplateExercise.id,
//                            exerciseId = exerciseWithSets.exercise.id,
//                            exerciseName = exerciseWithSets.exercise.name,
//                            exerciseImageUri = exerciseWithSets.exercise.imageUri,
//                            exerciseDefault = exerciseWithSets.exercise.default,
//                            exerciseOrder = exerciseWithSets.workoutTemplateExercise.exerciseOrder,
//                            notes = exerciseWithSets.workoutTemplateExercise.notes,
//                            sets = exerciseWithSets.sets.map { it.toWorkoutTemplateSet() } // Mapped to domain object
//                        )
//                    }
                    _uiState.update {
                        it.copy(
//                            name = templateWithDetails.workoutTemplate.name,
//                            description = templateWithDetails.workoutTemplate.description,
//                            imageUri = templateWithDetails.workoutTemplate.imageUri,
//                            default = templateWithDetails.workoutTemplate.default,
//                            exercises = detailedExercises,
                            isLoading = false,
                            isEditing = false
                            // TODO: Load persistedDefault values if reset functionality is added
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = Res.string.top_bar_title_workout_template_detail // TODO: Add a specific "template not found" error string
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = Res.string.top_bar_title_workout_template_detail // TODO: Add a specific error string for loading failure
                        // error = e.message // Consider using e.message for debugging, but user-friendly string for release
                    )
                }
            }
        }
    }

    fun onNameChange(name: String) {
        if (_uiState.value.isEditing) {
            _uiState.update { it.copy(name = name) }
            // TODO: Update isModifiedFromPersistedDefault if implementing reset
        }
    }

    fun onDescriptionChange(description: String) {
        if (_uiState.value.isEditing) {
            _uiState.update { it.copy(description = description) }
            // TODO: Update isModifiedFromPersistedDefault if implementing reset
        }
    }

    fun onImageUriChange(imageUri: String?) {
        if (_uiState.value.isEditing) {
            _uiState.update { it.copy(imageUri = imageUri) }
            // TODO: Update isModifiedFromPersistedDefault if implementing reset
        }
    }

    // TODO: Implement saveWorkoutTemplate, deleteWorkoutTemplate, toggleEditState, etc.
    // TODO: Add functions to manage exercises and sets within the template when editing (add, remove, reorder)
    // TODO: Implement resetToDefault if default templates can be modified and reset
}
