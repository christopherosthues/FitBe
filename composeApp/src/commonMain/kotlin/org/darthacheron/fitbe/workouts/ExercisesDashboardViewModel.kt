package org.darthacheron.fitbe.workouts

import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_exercises_dashboard
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.ui.BottomNavigationBarViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource

class ExercisesDashboardViewModel(
    topNavHostController: NavHostController,
    navHostController: NavHostController,
    topBarManager: TopBarManager
) : BottomNavigationBarViewModel(topNavHostController, navHostController, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_exercises_dashboard

    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard

    fun navigateToPrograms() {
        navHostController.navigate(Screen.ProgramOverview)
    }

    fun navigateToWorkoutsOverview() {
        navHostController.navigate(Screen.WorkoutsOverview)
    }

    fun navigateToExercisesOverview() {
        navHostController.navigate(Screen.ExercisesOverview)
    }

    fun navigateToEquipmentOverview() {
        navHostController.navigate(Screen.TrainingEquipmentOverview)
    }
}