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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

import org.darthacheron.fitbe.workouts.workouts.WorkoutExecutionRepository
import org.darthacheron.fitbe.workouts.workouts.WorkoutExecutionSession

@OptIn(ExperimentalUuidApi::class)
data class WorkoutOverviewUiState(
    val isLoading: Boolean = true,
    val scheduledSessions: List<WorkoutExecutionSession> = emptyList(),
    val pastSessions: List<WorkoutExecutionSession> = emptyList(),
    val error: StringResource? = null
)

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class WorkoutOverviewViewModel(
    private val workoutExecutionRepository: WorkoutExecutionRepository,
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
                        error = Res.string.workout_overview_error_no_profile_selected
                    )
                )
            } else {
                combine(
                    workoutExecutionRepository.getScheduledWorkoutExecutionSessions(profileId),
                    workoutExecutionRepository.getCompletedOrInProgressSessionsByProfileId(profileId)
                ) { scheduled, past ->
                    WorkoutOverviewUiState(
                        isLoading = false,
                        scheduledSessions = scheduled,
                        pastSessions = past
                    )
                }.catch { e ->
                    // You might want to log the exception e
                    emit(WorkoutOverviewUiState(isLoading = false, error = Res.string.workout_overview_error_loading_workouts))
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WorkoutOverviewUiState(isLoading = true)
        )

    fun onWorkoutSessionClicked(session: WorkoutExecutionSession) {
        if (session.startTimestamp == null && session.scheduledTimestamp != null) {
            // This is a scheduled session, perhaps navigate to a screen to start or edit it
            // navHostController.navigate(Screen.ScheduledWorkoutDetail(session.id.toString()))
            println("Scheduled workout session clicked: ${session.id} (Navigation to detail/start screen not yet implemented)")
        } else {
            // This is a past or in-progress session, navigate to its details/summary
            // navHostController.navigate(Screen.WorkoutExecutionDetail(session.id.toString()))
            println("Past workout session clicked: ${session.id} (Navigation to detail screen not yet implemented)")
        }
    }

    fun onStartNewWorkoutClicked() {
        // navHostController.navigate(Screen.NewWorkoutExecution()) // Or Screen.SelectWorkoutTemplateToStart()
        println("Start new workout clicked (Navigation not yet implemented)")
    }

    fun onScheduleNewWorkoutClicked() {
        // navHostController.navigate(Screen.ScheduleNewWorkout()) // TODO: Define screen for scheduling a new workout
        println("Schedule new workout clicked (Navigation not yet implemented)")
    }

    // Example of how to start a scheduled workout
    fun startScheduledWorkoutNow(sessionId: Uuid) {
        viewModelScope.launch {
            // You might want to show a loading indicator here
            try {
                workoutExecutionRepository.startScheduledWorkoutExecution(sessionId, Clock.System.now())
                // Optionally navigate to the active workout screen or refresh current view
                println("Scheduled workout $sessionId started.")
            } catch (e: Exception) {
                // Handle error (e.g., show a snackbar)
                println("Error starting scheduled workout $sessionId: ${e.message}")
            }
        }
    }
}
