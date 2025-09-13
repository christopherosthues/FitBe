package org.darthacheron.fitbe.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import org.darthacheron.fitbe.workouts.ExercisesDashboardView
import org.darthacheron.fitbe.workouts.ExercisesDashboardViewModel
import org.darthacheron.fitbe.workouts.exercises.ExerciseDetailView
import org.darthacheron.fitbe.workouts.exercises.ExerciseDetailViewModel
import org.darthacheron.fitbe.workouts.exercises.ExercisesView
import org.darthacheron.fitbe.workouts.exercises.ExercisesViewModel
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipmentDetailView
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipmentDetailViewModel
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipmentView
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipmentViewModel
// New imports for WorkoutOverview
import org.darthacheron.fitbe.workouts.workouts.PerformedWorkoutsOverviewView
import org.darthacheron.fitbe.workouts.workouts.PerformedWorkoutsOverviewViewModel
import org.darthacheron.fitbe.health.HealthOverviewView
import org.darthacheron.fitbe.health.HealthOverviewViewModel
import org.darthacheron.fitbe.health.beverages.BeverageOverviewView
import org.darthacheron.fitbe.health.beverages.BeverageOverviewViewModel
import org.darthacheron.fitbe.health.beverages.BeverageView
import org.darthacheron.fitbe.health.beverages.BeverageViewModel
import org.darthacheron.fitbe.health.sleep.SleepOverviewView
import org.darthacheron.fitbe.health.sleep.SleepViewModel
import org.darthacheron.fitbe.health.steps.StepsView
import org.darthacheron.fitbe.health.steps.StepsViewModel
import org.darthacheron.fitbe.health.weight.WeightOverviewView
import org.darthacheron.fitbe.health.weight.WeightOverviewViewModel
import org.darthacheron.fitbe.home.HomeView
import org.darthacheron.fitbe.home.HomeViewModel
import org.darthacheron.fitbe.profile.ProfileView
import org.darthacheron.fitbe.profile.ProfileViewModel
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateDetailView
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateDetailViewModel
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplatesOverviewView
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplatesOverviewViewModel
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun BottomBarNavGraph(
    topNavHostController: NavHostController,
    bottomBarNavHostController: NavHostController,
    paddingValues: PaddingValues,
) {
    NavHost(
        navController = bottomBarNavHostController,
        startDestination = Screen.BottomBarGraph,
        modifier = Modifier.padding(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding()
        )
    ) {
        navigation<Screen.BottomBarGraph>(
            startDestination = Screen.Home
        ) {
            composable<Screen.Home>{
                val viewModel = koinViewModel<HomeViewModel>(
                    parameters = { parametersOf(topNavHostController, bottomBarNavHostController) }
                )
                HomeView(viewModel)
            }
            composable<Screen.ExercisesDashboard> {
                val viewModel = koinViewModel<ExercisesDashboardViewModel>(
                    parameters = { parametersOf(topNavHostController, bottomBarNavHostController) }
                )
                ExercisesDashboardView(viewModel)
            }
            composable<Screen.Health> {
                val viewModel = koinViewModel<HealthOverviewViewModel>(
                    parameters = { parametersOf(topNavHostController, bottomBarNavHostController) }
                )
                val settingsRepository = getKoin().get<SettingsRepository>()
                HealthOverviewView(viewModel, settingsRepository)
            }
            composable<Screen.Profile> {
                val viewModel = koinViewModel<ProfileViewModel>(
                    parameters = { parametersOf(topNavHostController, bottomBarNavHostController) }
                )
                val settingsRepository = getKoin().get<SettingsRepository>()
                ProfileView(viewModel, settingsRepository)
            }
        }
        composable<Screen.ExercisesOverview> {
            val viewModel = koinViewModel<ExercisesViewModel>(
                parameters = { parametersOf(bottomBarNavHostController) }
            )
            ExercisesView(viewModel)
        }
        composable<Screen.ExerciseDetail> { backStackEntry ->
            val exerciseDetailRoute: Screen.ExerciseDetail = backStackEntry.toRoute()
            val viewModel = koinViewModel<ExerciseDetailViewModel>(
                parameters = { parametersOf(bottomBarNavHostController) }
            )
            val id = exerciseDetailRoute.id.toUuidOrNull()
            ExerciseDetailView(id, viewModel)
        }
        composable<Screen.TrainingEquipmentOverview>{
            val viewModel = koinViewModel<TrainingEquipmentViewModel>(
                parameters = { parametersOf(bottomBarNavHostController) }
            )
            TrainingEquipmentView(viewModel)
        }
        composable<Screen.TrainingEquipmentDetail> { backStackEntry ->
            val trainingEquipmentDetailRoute: Screen.TrainingEquipmentDetail = backStackEntry.toRoute()
            val viewModel = koinViewModel<TrainingEquipmentDetailViewModel>(
                parameters = { parametersOf(bottomBarNavHostController) }
            )
            val id = trainingEquipmentDetailRoute.id.toUuidOrNull()
            TrainingEquipmentDetailView(id, viewModel)
        }
        composable<Screen.Sleeps> {
            val viewModel = koinViewModel<SleepViewModel>()
            SleepOverviewView(viewModel)
        }
        composable<Screen.Steps> {
            val viewModel = koinViewModel<StepsViewModel>()
            StepsView(viewModel)
        }
        composable<Screen.BeveragesOverview> {
            val viewModel = koinViewModel<BeverageOverviewViewModel>()
            BeverageOverviewView(viewModel)
        }
        composable<Screen.Beverages> {
            val viewModel = koinViewModel<BeverageViewModel>()
            BeverageView(viewModel)
        }
        composable<Screen.BodyWeights> {
            val viewModel = koinViewModel<WeightOverviewViewModel>()
            val settingsRepository = getKoin().get<SettingsRepository>()
            WeightOverviewView(viewModel, settingsRepository)
        }
        composable<Screen.WorkoutTemplatesOverview> {
            val viewModel = koinViewModel<WorkoutTemplatesOverviewViewModel>(
                parameters = { parametersOf(bottomBarNavHostController) }
            )
            WorkoutTemplatesOverviewView(viewModel)
        }
        composable<Screen.WorkoutTemplateDetail> { backStackEntry ->
            val workoutTemplateRoute: Screen.TrainingEquipmentDetail = backStackEntry.toRoute()
            val id = workoutTemplateRoute.id.toUuidOrNull()
            val viewModel = koinViewModel<WorkoutTemplateDetailViewModel>(
                parameters = { parametersOf(bottomBarNavHostController) }
            )
            WorkoutTemplateDetailView(id, viewModel)
        }
        composable<Screen.PerformedWorkoutsOverview> {
            val viewModel = koinViewModel<PerformedWorkoutsOverviewViewModel>(
                parameters = { parametersOf(bottomBarNavHostController) }
            )
            PerformedWorkoutsOverviewView(viewModel)
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
fun String?.toUuidOrNull(): Uuid? {
    if (this == null) {
        return null
    }
    return runCatching {
        Uuid.parse(this)
    }.onFailure {
        println("Failed to parse UUID from string '$this': ${it.message}")
    }.getOrNull()
}
