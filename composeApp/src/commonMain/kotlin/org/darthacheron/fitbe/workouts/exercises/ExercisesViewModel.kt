package org.darthacheron.fitbe.workouts.exercises

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.exercises_error_favorites
import fitbe.composeapp.generated.resources.exercises_error_loading
import fitbe.composeapp.generated.resources.exercises_error_toggle_favorite
import fitbe.composeapp.generated.resources.top_bar_title_exercises
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class ExercisesViewModel(
    private val exerciseRepository: ExerciseRepository,
    settingsRepository: SettingsRepository,
    private val navHostController: NavHostController,
    topBarManager: TopBarManager
) : FilterableViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_exercises

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard

    private val _isLoadingExercises = MutableStateFlow(true)
    private val _isLoadingFavorites = MutableStateFlow(true)
    private val _exerciseListErrorMessage = MutableStateFlow<StringResource?>(null)
    private val _favoriteStateErrorMessage = MutableStateFlow<StringResource?>(null)

    private val _selectedMuscleGroups = MutableStateFlow<Set<MuscleGroup>>(emptySet())
    val selectedMuscleGroups: StateFlow<Set<MuscleGroup>> = _selectedMuscleGroups.asStateFlow()

    private val _selectedRecommendedForItems = MutableStateFlow<Set<RecommendedFor>>(emptySet())
    val selectedRecommendedForItems: StateFlow<Set<RecommendedFor>> = _selectedRecommendedForItems.asStateFlow()

    private val _selectedExerciseTypes = MutableStateFlow<Set<ExerciseType>>(emptySet())
    val selectedExerciseTypes: StateFlow<Set<ExerciseType>> = _selectedExerciseTypes.asStateFlow()

    private val currentProfileIdFlow: StateFlow<Uuid?> = settingsRepository.getSettingsFlow()
        .map { it.selectedProfileId }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val internalExercisesFlow: StateFlow<List<Exercise>> =
        exerciseRepository.getAllExercises()
            .onStart {
                _isLoadingExercises.value = true
                _exerciseListErrorMessage.value = null
            }
            .map { exercises ->
                _isLoadingExercises.value = false
                exercises
            }
            .catch { e ->
                // Log.e("ExercisesViewModel", "Error loading exercises", e)
                _isLoadingExercises.value = false
                _exerciseListErrorMessage.value = Res.string.exercises_error_loading
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
                exerciseRepository.getFavoriteExerciseIds(profileId)
                    .map { it.toSet() }
                    .onStart {
                        _isLoadingFavorites.value = true
                        _favoriteStateErrorMessage.value = null // Clear specific favorite error on new load attempt
                    }
                    .map { favIds ->
                        _isLoadingFavorites.value = false
                        favIds
                    }
                    .catch { e ->
                        // Log.e("ExercisesViewModel", "Error loading favorite exercises", e)
                        _isLoadingFavorites.value = false
                        _favoriteStateErrorMessage.value = Res.string.exercises_error_favorites
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

    // Combine the first 5 flows into a temporary data structure
    private val baseUiDataFlow: Flow<BaseUiData> = combine(
        internalExercisesFlow,      // Flow 1: List<Exercise>
        internalFavoritesFlow,       // Flow 2: Set<Uuid>
        _isLoadingExercises,         // Flow 3: Boolean
        _isLoadingFavorites,         // Flow 4: Boolean
        _exerciseListErrorMessage    // Flow 5: StringResource?
    ) { exercises, favorites, isLoadingEx, isLoadingFav, exerciseError ->
        BaseUiData(exercises, favorites, isLoadingEx, isLoadingFav, exerciseError)
    }

    val uiState: StateFlow<ExercisesScreenUiState> = combine(
        baseUiDataFlow,             // Combined Flow 1
        _favoriteStateErrorMessage,  // Flow 2
        _selectedMuscleGroups,        // Flow 3
        _selectedRecommendedForItems,     // Flow 4
        _selectedExerciseTypes        // Flow 5
    ) { baseData, favError, muscleGroups, recommendedForItems, exerciseTypes ->
        ExercisesScreenUiState(
            isLoading = baseData.isLoadingExercises || baseData.isLoadingFavorites,
            rawExercises = baseData.exercises,
            favoriteExerciseIds = baseData.favorites,
            exerciseListError = baseData.exerciseListError,
            favoriteStateError = favError,
            selectedMuscleGroups = muscleGroups,
            selectedRecommendedForItems = recommendedForItems,
            selectedExerciseTypes = exerciseTypes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExercisesScreenUiState(isLoading = true))

    // Helper data class for the first combine operation
    private data class BaseUiData(
        val exercises: List<Exercise>,
        val favorites: Set<Uuid>,
        val isLoadingExercises: Boolean,
        val isLoadingFavorites: Boolean,
        val exerciseListError: StringResource?
    )

    fun navigateToExerciseDetail(id: Uuid?) {
        navHostController.navigate(Screen.ExerciseDetail(id?.toString()))
    }

    fun toggleFavorite(exerciseId: Uuid) {
        viewModelScope.launch {
            val profileId = currentProfileIdFlow.value
            if (profileId != null) {
                _favoriteStateErrorMessage.value = null // Clear previous toggle error
                try {
                    val isCurrentlyFavorite = uiState.value.favoriteExerciseIds.contains(exerciseId)
                    if (isCurrentlyFavorite) {
                        exerciseRepository.removeFavorite(profileId, exerciseId)
                    } else {
                        exerciseRepository.addFavorite(profileId, exerciseId)
                    }
                } catch (e: Exception) {
                    // Log.e("ExercisesViewModel", "Error toggling favorite", e)
                    _favoriteStateErrorMessage.value = Res.string.exercises_error_toggle_favorite
                }
            }
        }
    }

    fun applyFilters(
        muscleGroups: Set<MuscleGroup>,
        recommendedForItems: Set<RecommendedFor>,
        exerciseTypes: Set<ExerciseType>
    ) {
        _selectedMuscleGroups.value = muscleGroups
        _selectedRecommendedForItems.value = recommendedForItems
        _selectedExerciseTypes.value = exerciseTypes
    }

    fun removeMuscleGroupFilter(muscleGroup: MuscleGroup) {
        _selectedMuscleGroups.value = _selectedMuscleGroups.value - muscleGroup
    }

    fun removeRecommendedForFilter(recommendedFor: RecommendedFor) {
        _selectedRecommendedForItems.value = _selectedRecommendedForItems.value - recommendedFor
    }

    fun removeExerciseTypeFilter(exerciseType: ExerciseType) {
        _selectedExerciseTypes.value = _selectedExerciseTypes.value - exerciseType
    }
    
    fun clearAllFilters() {
        _selectedMuscleGroups.value = emptySet()
        _selectedRecommendedForItems.value = emptySet()
        _selectedExerciseTypes.value = emptySet()
    }

    fun clearErrorMessage() {
        _exerciseListErrorMessage.value = null
        _favoriteStateErrorMessage.value = null
    }
}
