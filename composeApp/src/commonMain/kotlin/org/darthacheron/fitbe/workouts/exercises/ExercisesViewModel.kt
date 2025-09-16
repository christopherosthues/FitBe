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
data class ExercisesScreenUiState(
    val isLoading: Boolean = true, // Combined loading state
    val rawExercises: List<Exercise> = emptyList(),
    val favoriteExerciseIds: Set<Uuid> = emptySet(),
    val exerciseListError: StringResource? = null,
    val favoriteStateError: StringResource? = null
)

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

    // Combine the first 5 flows
    private val combinedFiveFlows: Flow<Pair<Pair<List<Exercise>, Set<Uuid>>, Triple<Boolean, Boolean, StringResource?>>> = combine(
        internalExercisesFlow,      // Flow 1: List<Exercise>
        internalFavoritesFlow,       // Flow 2: Set<Uuid>
        _isLoadingExercises,         // Flow 3: Boolean
        _isLoadingFavorites,         // Flow 4: Boolean
        _exerciseListErrorMessage    // Flow 5: StringResource?
    ) { exercises: List<Exercise>, favorites: Set<Uuid>, isLoadingEx: Boolean, isLoadingFav: Boolean, exerciseError: StringResource? ->
        // Temporary data holder for the first 5 values
        Pair(
            Pair<List<Exercise>, Set<Uuid>>(exercises, favorites),
            Triple<Boolean, Boolean, StringResource?>(isLoadingEx, isLoadingFav, exerciseError)
        )
    }

    val uiState: StateFlow<ExercisesScreenUiState> = combinedFiveFlows.combine(
        _favoriteStateErrorMessage   // Flow 6: StringResource?
    ) { intermediateData: Pair<Pair<List<Exercise>, Set<Uuid>>, Triple<Boolean, Boolean, StringResource?>>, 
        favError: StringResource? ->
        val (pair1: Pair<List<Exercise>, Set<Uuid>>, triple1: Triple<Boolean, Boolean, StringResource?>) = intermediateData
        val (exercises: List<Exercise>, favorites: Set<Uuid>) = pair1
        val (isLoadingEx: Boolean, isLoadingFav: Boolean, exerciseError: StringResource?) = triple1
        
        ExercisesScreenUiState(
            isLoading = isLoadingEx || isLoadingFav,
            rawExercises = exercises,
            favoriteExerciseIds = favorites,
            exerciseListError = exerciseError,
            favoriteStateError = favError
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExercisesScreenUiState(isLoading = true))


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

    fun clearErrorMessage() {
        _exerciseListErrorMessage.value = null
        _favoriteStateErrorMessage.value = null
    }
}
