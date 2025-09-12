package org.darthacheron.fitbe.workouts.templates

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
// Assuming general resources might be needed, adjust as necessary
// import fitbe.composeapp.generated.resources.Res
// import fitbe.composeapp.generated.resources.some_string_resource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.darthacheron.fitbe.navigation.Screen // Or your specific navigation class
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// Assuming string resources for errors or titles might be needed
// For now, using placeholder or direct strings if no resources are obvious

@OptIn(ExperimentalUuidApi::class)
data class WorkoutTemplatesUiState(
    val isLoading: Boolean = true,
    val templates: List<WorkoutTemplate> = emptyList(),
    val error: String? = null // Using String for error for now, can be StringResource
)

@OptIn(ExperimentalUuidApi::class)
class WorkoutTemplatesViewModel(
    private val workoutTemplateRepository: WorkoutTemplateRepository,
    private val navHostController: NavHostController, // For navigation actions
    topBarManager: TopBarManager
    // private val settingsRepository: SettingsRepository, // If profile needed for starting execution from template
) : FitBeViewModel(topBarManager) {

    // override val title: StringResource = Res.string.top_bar_title_workout_templates // Placeholder for actual resource
    override val bottomBarSelected: Screen? = Screen.ExercisesDashboard // Or a new dedicated screen for templates if it exists
    override val backNavigationIconVisible: Boolean? = true

    val uiState: StateFlow<WorkoutTemplatesUiState> = workoutTemplateRepository.getAllWorkoutTemplatesWithExercisesAndSets()
        .map { templatesList ->
            WorkoutTemplatesUiState(isLoading = false, templates = templatesList)
        }
        .catch { e ->
            emit(WorkoutTemplatesUiState(isLoading = false, error = "Error loading workout templates: ${e.message}"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WorkoutTemplatesUiState(isLoading = true)
        )

    fun onTemplateClicked(templateId: Uuid) {
        // navHostController.navigate(Screen.WorkoutTemplateDetail(templateId.toString())) // TODO: Define WorkoutTemplateDetail screen
        println("Workout template clicked: $templateId (Navigation to detail screen not yet implemented)")
    }

    fun onCreateNewTemplateClicked() {
        // navHostController.navigate(Screen.CreateWorkoutTemplate()) // TODO: Define CreateWorkoutTemplate screen
        println("Create new workout template clicked (Navigation not yet implemented)")
    }

    fun onStartWorkoutFromTemplateClicked(templateId: Uuid) {
        // This might navigate to a screen that pre-populates a new workout execution session
        // with data from the selected template.
        // Might need selectedProfileId from SettingsRepository here.
        println("Start workout from template clicked: $templateId (Functionality not yet implemented)")
    }
}
