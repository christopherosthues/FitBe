package org.darthacheron.fitbe.workouts.exercises

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_exercises
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
