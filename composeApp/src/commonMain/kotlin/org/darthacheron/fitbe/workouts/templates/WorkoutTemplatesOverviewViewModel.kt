package org.darthacheron.fitbe.workouts.templates

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_workout_templates
import fitbe.composeapp.generated.resources.workout_template_error_favorites
import fitbe.composeapp.generated.resources.workout_template_error_toggle_favorite
import fitbe.composeapp.generated.resources.workout_overview_error_loading_workouts
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.FilterableViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class WorkoutTemplatesUiState(
    val isLoading: Boolean = true,
    val rawWorkoutTemplateList: List<WorkoutTemplate> = emptyList(),
    val favoriteWorkoutTemplateIds: Set<Uuid> = emptySet(),
    val workoutTemplateListError: StringResource? = null,
    val favoriteStateError: StringResource? = null
)

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class WorkoutTemplatesOverviewViewModel(
    private val workoutTemplateRepository: WorkoutTemplateRepository,
    private val navHostController: NavHostController,
    topBarManager: TopBarManager,
    settingsRepository: SettingsRepository,
) : FilterableViewModel(topBarManager) {

    override val title: StringResource
        get() = Res.string.top_bar_title_workout_templates
    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard // Or Screen.WorkoutsDashboard if that exists
    override val backNavigationIconVisible: Boolean?
        get() = true

    private val _isLoadingWorkoutTemplates = MutableStateFlow(true)
    private val _isLoadingFavorites = MutableStateFlow(true)
    private val _workoutTemplateListErrorMessage = MutableStateFlow<StringResource?>(null)
    private val _favoriteStateErrorMessage = MutableStateFlow<StringResource?>(null)

    private val currentProfileIdFlow: StateFlow<Uuid?> = settingsRepository.getSettingsFlow()
        .map { it.selectedProfileId }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val internalWorkoutTemplatesFlow: StateFlow<List<WorkoutTemplate>> =
        workoutTemplateRepository.getAllWorkoutTemplatesWithExercisesAndSets()
            .onStart {
                _isLoadingWorkoutTemplates.value = true
                _workoutTemplateListErrorMessage.value = null
            }
            .map { templates ->
                _isLoadingWorkoutTemplates.value = false
                templates
            }
            .catch { e ->
                _isLoadingWorkoutTemplates.value = false
                _workoutTemplateListErrorMessage.value = Res.string.workout_overview_error_loading_workouts
                emit(emptyList())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val internalFavoritesFlow: StateFlow<Set<Uuid>> = currentProfileIdFlow
        .flatMapLatest { profileId ->
            if (profileId != null) {
                workoutTemplateRepository.getFavoriteWorkoutTemplateIds(profileId)
                    .map { it.toSet() }
                    .onStart {
                        _isLoadingFavorites.value = true
                        _favoriteStateErrorMessage.value = null
                    }
                    .map { favIds ->
                        _isLoadingFavorites.value = false
                        favIds
                    }
                    .catch { e ->
                        _isLoadingFavorites.value = false
                        _favoriteStateErrorMessage.value = Res.string.workout_template_error_favorites
                        emit(emptySet())
                    }
            } else {
                _isLoadingFavorites.value = false
                flowOf(emptySet())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    private val _errorMessagesFlow = combine(
        _workoutTemplateListErrorMessage,
        _favoriteStateErrorMessage
    ) { listError, favError ->
        Pair(listError, favError)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Pair<StringResource?, StringResource?>(null, null)
    )

    val uiState: StateFlow<WorkoutTemplatesUiState> = combine(
        internalWorkoutTemplatesFlow,
        internalFavoritesFlow,
        _isLoadingWorkoutTemplates,
        _isLoadingFavorites,
        _errorMessagesFlow
    ) { templates, favorites, isLoadingTemplates, isLoadingFavs, errorMessages ->
        val (listError, favError) = errorMessages
        WorkoutTemplatesUiState(
            isLoading = isLoadingTemplates || isLoadingFavs,
            rawWorkoutTemplateList = templates,
            favoriteWorkoutTemplateIds = favorites,
            workoutTemplateListError = listError,
            favoriteStateError = favError
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WorkoutTemplatesUiState(isLoading = true)
    )

    fun navigateToWorkoutTemplateDetail(templateId: Uuid?) {
        navHostController.navigate(Screen.WorkoutTemplateDetail(templateId.toString()))
    }

    fun addFavorite(workoutTemplateId: Uuid) {
        viewModelScope.launch {
            val profileId = currentProfileIdFlow.value
            if (profileId != null) {
                _favoriteStateErrorMessage.value = null
                try {
                    workoutTemplateRepository.addFavorite(profileId, workoutTemplateId)
                } catch (e: Exception) {
                    _favoriteStateErrorMessage.value = Res.string.workout_template_error_toggle_favorite
                }
            }
        }
    }

    fun removeFavorite(workoutTemplateId: Uuid) {
        viewModelScope.launch {
            val profileId = currentProfileIdFlow.value
            if (profileId != null) {
                _favoriteStateErrorMessage.value = null
                try {
                    workoutTemplateRepository.removeFavorite(profileId, workoutTemplateId)
                } catch (e: Exception) {
                    _favoriteStateErrorMessage.value = Res.string.workout_template_error_toggle_favorite
                }
            }
        }
    }

    fun clearErrorMessage() {
        _workoutTemplateListErrorMessage.value = null
        _favoriteStateErrorMessage.value = null
    }
}
