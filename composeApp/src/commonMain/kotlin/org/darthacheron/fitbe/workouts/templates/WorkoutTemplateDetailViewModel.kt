package org.darthacheron.fitbe.workouts.templates

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_favorite
import fitbe.composeapp.generated.resources.ic_favorite_border
import fitbe.composeapp.generated.resources.top_bar_title_workout_template_detail
// Placeholder for new string resources - these should be created
// import fitbe.composeapp.generated.resources.workout_template_detail_content_description_add_favorite
// import fitbe.composeapp.generated.resources.workout_template_detail_content_description_remove_favorite
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.settings.SettingsRepository
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
    val isFavorite: Boolean = false,
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
    val sets: List<WorkoutTemplateSet> = emptyList()
)

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class WorkoutTemplateDetailViewModel(
    private val workoutTemplateRepository: WorkoutTemplateRepository,
    private val settingsRepository: SettingsRepository, // Added SettingsRepository
    private val navHostController: NavHostController,
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_workout_template_detail

    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard // Or Screen.WorkoutsDashboard

    override val backNavigationIconVisible: Boolean?
        get() = true

    private val _uiState = MutableStateFlow(WorkoutTemplateDetailUiState())
    val uiState: StateFlow<WorkoutTemplateDetailUiState> = _uiState.asStateFlow()

    private val currentProfileId: StateFlow<Uuid?> = settingsRepository.getSettingsFlow()
        .map { it.selectedProfileId }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val isFavoriteFlow: StateFlow<Boolean> =
        combine(
            uiState.map { it.templateId }.distinctUntilChanged(),
            currentProfileId
        ) { templateId, profileId ->
            if (templateId != null && profileId != null) {
                workoutTemplateRepository.isFavorite(profileId, templateId)
            } else {
                flowOf(false)
            }
        }.flatMapLatest { it }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        viewModelScope.launch {
            isFavoriteFlow.collect { isFav ->
                _uiState.update { it.copy(isFavorite = isFav) }
                updateTopBarConfig() // Update top bar when favorite status changes
            }
        }
    }

    override val actions: List<TopBarAction>
        get() {
            val currentTemplateId = _uiState.value.templateId
            val currentProfId = currentProfileId.value
            val isCurrentlyFavorite = _uiState.value.isFavorite

            val favoriteAction = TopBarAction(
                icon = if (isCurrentlyFavorite) Res.drawable.ic_favorite else Res.drawable.ic_favorite_border,
                // TODO: Replace with actual string resources
                contentDescription = if (isCurrentlyFavorite) Res.string.top_bar_title_workout_template_detail else Res.string.top_bar_title_workout_template_detail, 
                onClick = { toggleFavorite() },
                isVisible = currentTemplateId != null && currentProfId != null
            )
            // TODO: Add other actions like Edit, Save, Delete etc.
            return listOf(favoriteAction)
        }

    private fun toggleFavorite() {
        val templateId = _uiState.value.templateId
        val profileId = currentProfileId.value

        if (templateId != null && profileId != null) {
            viewModelScope.launch {
                if (_uiState.value.isFavorite) {
                    workoutTemplateRepository.removeFavorite(profileId, templateId)
                } else {
                    workoutTemplateRepository.addFavorite(profileId, templateId)
                }
                // isFavorite state will be updated by isFavoriteFlow collection
            }
        }
    }

    fun loadWorkoutTemplate(templateId: Uuid?) {
        if (templateId == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isEditing = true, 
                    templateId = null,
                    name = "",
                    description = null,
                    imageUri = null,
                    exercises = emptyList(),
                    default = false,
                    error = null,
                    isFavorite = false // Reset favorite for new template
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, templateId = templateId, error = null) }
        viewModelScope.launch {
            try {
                val templateWithDetails = workoutTemplateRepository.getWorkoutTemplateWithExercisesAndSets(templateId).firstOrNull()

                if (templateWithDetails != null) {
                    val detailedExercises = templateWithDetails.exercises.map { exerciseWithSets ->
//                        WorkoutTemplateExerciseWithDetails(
//                            templateExerciseId = exerciseWithSets.workoutTemplateExercise.id,
//                            exerciseId = exerciseWithSets.exercise.id,
//                            exerciseName = exerciseWithSets.exercise.name,
//                            exerciseImageUri = exerciseWithSets.exercise.imageUri,
//                            exerciseDefault = exerciseWithSets.exercise.default,
//                            exerciseOrder = exerciseWithSets.workoutTemplateExercise.exerciseOrder,
//                            notes = exerciseWithSets.workoutTemplateExercise.notes,
//                            sets = exerciseWithSets.sets.map { it.toWorkoutTemplateSet() }
//                        )
                    }
                    _uiState.update {
                        it.copy(
//                            name = templateWithDetails.workoutTemplate.name,
//                            description = templateWithDetails.workoutTemplate.description,
//                            imageUri = templateWithDetails.workoutTemplate.imageUri,
//                            default = templateWithDetails.workoutTemplate.default,
//                            exercises = detailedExercises,
                            isLoading = false,
                            isEditing = false
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
                    )
                }
            }
        }
    }

    fun onNameChange(name: String) {
        if (_uiState.value.isEditing) {
            _uiState.update { it.copy(name = name) }
        }
    }

    fun onDescriptionChange(description: String) {
        if (_uiState.value.isEditing) {
            _uiState.update { it.copy(description = description) }
        }
    }

    fun onImageUriChange(imageUri: String?) {
        if (_uiState.value.isEditing) {
            _uiState.update { it.copy(imageUri = imageUri) }
        }
    }
}
