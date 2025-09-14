package org.darthacheron.fitbe.workouts.exercises

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_exercises
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.FitBeViewModel
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
) : FitBeViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_exercises

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard

    private val _filterText = MutableStateFlow("")
    val filterText: StateFlow<String> = _filterText.asStateFlow()

    val allExercises: StateFlow<List<Exercise>> =
        exerciseRepository.getAllExercises()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val currentProfileId: StateFlow<Uuid?> = settingsRepository.getSettingsFlow()
        .map { it.selectedProfileId }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val favoriteExerciseIds: StateFlow<Set<Uuid>> = currentProfileId
        .flatMapLatest { profileId ->
            if (profileId != null) {
                exerciseRepository.getFavoriteExerciseIds(profileId).map { it.toSet() }
            } else {
                flowOf(emptySet())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    @Composable
    fun toExerciseWithLocalizedName(exercises: List<Exercise>): List<Exercise> {
        return exercises.map {
            Exercise(
                id = it.id,
                name = getExerciseName(it.name, it.default),
                guide = it.guide,
                targetMuscleGroups = it.targetMuscleGroups,
                imageUri = it.imageUri,
                default = it.default,
                recommendedFor = it.recommendedFor,
                exerciseType = it.exerciseType,
                dateUtc = it.dateUtc
            )
        }
    }

    val filteredExercises: StateFlow<List<Exercise>> = combine(
        allExercises,
        filterText,
        favoriteExerciseIds
    ) { exercises, filter, favorites ->
        val localizedExercises = toExerciseWithLocalizedName(exercises)
        val filteredList = if (filter.isBlank()) {
            localizedExercises
        } else {
            localizedExercises.filter {
                it.name.contains(filter, ignoreCase = true)
            }
        }
        filteredList.sortedWith(
            compareByDescending<Exercise> { favorites.contains(it.id) }
                .thenBy { it.name }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onFilterTextChanged(text: String) {
        _filterText.value = text
    }

    fun navigateToExerciseDetail(id: Uuid?) {
        navHostController.navigate(Screen.ExerciseDetail(id?.toString()))
    }

    fun toggleFavorite(exerciseId: Uuid) {
        viewModelScope.launch {
            val profileId = currentProfileId.value
            if (profileId != null) {
                val isCurrentlyFavorite = favoriteExerciseIds.value.contains(exerciseId)
                if (isCurrentlyFavorite) {
                    exerciseRepository.removeFavorite(profileId, exerciseId)
                } else {
                    exerciseRepository.addFavorite(profileId, exerciseId)
                }
            }
        }
    }
}
