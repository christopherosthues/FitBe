package org.darthacheron.fitbe.workouts.workouts

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_workout_overview
import fitbe.composeapp.generated.resources.workout_overview_error_loading_workouts
import fitbe.composeapp.generated.resources.workout_overview_error_no_profile_selected
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// Import the new repository and domain model
import org.darthacheron.fitbe.workouts.workouts.WorkoutExecutionRepository
import org.darthacheron.fitbe.workouts.workouts.WorkoutExecutionSession

@OptIn(ExperimentalUuidApi::class)
data class WorkoutOverviewUiState(
    val isLoading: Boolean = true,
    val workoutSessions: List<WorkoutExecutionSession> = emptyList(), // Updated to WorkoutExecutionSession
    val error: StringResource? = null
)

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class WorkoutOverviewViewModel(
    private val workoutExecutionRepository: WorkoutExecutionRepository, // Updated repository
    private val settingsRepository: SettingsRepository,
    private val navHostController: NavHostController,
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {

    override val title: StringResource = Res.string.top_bar_title_workout_overview
    override val bottomBarSelected: Screen? = Screen.ExercisesDashboard
    override val backNavigationIconVisible: Boolean? = true

    val uiState: StateFlow<WorkoutOverviewUiState> = settingsRepository.getSettingsFlow()
        .map { it.selectedProfileId }
        .flatMapLatest { profileId ->
            if (profileId == null) {
                flowOf(
                    WorkoutOverviewUiState(
                        isLoading = false,
                        workoutSessions = emptyList(),
                        error = Res.string.workout_overview_error_no_profile_selected
                    )
                )
            } else {
                // Use the new repository and its methods
                workoutExecutionRepository.getWorkoutExecutionSessionsByProfileId(profileId)
                    .map { sessions ->
                        WorkoutOverviewUiState(
                            isLoading = false,
                            workoutSessions = sessions // Already List<WorkoutExecutionSession>
                        )
                    }
                    .catch { e ->
                         emit(WorkoutOverviewUiState(isLoading = false, error = Res.string.workout_overview_error_loading_workouts))
                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WorkoutOverviewUiState(isLoading = true) // Initial state is loading
        )

    fun onWorkoutSessionClicked(sessionId: Uuid) {
        // navHostController.navigate(Screen.WorkoutExecutionDetail(sessionId.toString())) // TODO: Define WorkoutExecutionDetail screen
        println("Workout session clicked: $sessionId (Navigation to detail not yet implemented)")
    }

    fun onStartNewWorkoutClicked() {
        // navHostController.navigate(Screen.NewWorkoutExecution()) // TODO: Define NewWorkoutExecution screen
        println("Start new workout clicked (Navigation not yet implemented)")
    }
}
