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
import org.darthacheron.fitbe.exercises.ExercisesDashboardView
import org.darthacheron.fitbe.exercises.exercises.ExerciseDetailView
import org.darthacheron.fitbe.exercises.exercises.ExerciseDetailViewModel
import org.darthacheron.fitbe.exercises.exercises.ExercisesView
import org.darthacheron.fitbe.exercises.exercises.ExercisesViewModel
import org.darthacheron.fitbe.exercises.equipment.TrainingEquipmentDetailView
import org.darthacheron.fitbe.exercises.equipment.TrainingEquipmentDetailViewModel
import org.darthacheron.fitbe.exercises.equipment.TrainingEquipmentView
import org.darthacheron.fitbe.exercises.equipment.TrainingEquipmentViewModel
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
import org.darthacheron.fitbe.ui.TopBarManager // Added import
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
// @Preview // Preview might be complex with TopBarManager injection
@Composable
fun BottomBarNavGraph(
    navHostController: NavHostController, 
    paddingValues: PaddingValues,
    topBarManager: TopBarManager // Added parameter
) {
    NavHost(
        navController = navHostController,
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
                val viewModel = koinViewModel<HomeViewModel>()
                HomeView(viewModel, navHostController, topBarManager)
            }
            composable<Screen.ExercisesDashboard> {
                ExercisesDashboardView(navHostController, topBarManager)
            }
            composable<Screen.Health> {
                val viewModel = koinViewModel<HealthOverviewViewModel>()
                val settingsRepository = getKoin().get<SettingsRepository>()
                HealthOverviewView(viewModel, settingsRepository, navHostController, topBarManager)
            }
            composable<Screen.Profile> {
                val viewModel = koinViewModel<ProfileViewModel>()
                val settingsRepository = getKoin().get<SettingsRepository>()
                ProfileView(viewModel, settingsRepository, topBarManager)
            }
        }
        composable<Screen.Exercises> {
            val viewModel = koinViewModel<ExercisesViewModel>()
            ExercisesView(viewModel, navHostController, topBarManager)
        }
        composable<Screen.ExerciseDetail> { backStackEntry ->
            val addEditExerciseRoute: Screen.ExerciseDetail = backStackEntry.toRoute()
            val viewModel = koinViewModel<ExerciseDetailViewModel>()
            val id = if (addEditExerciseRoute.id != null) Uuid.parse(addEditExerciseRoute.id) else null
            ExerciseDetailView(id, viewModel, navHostController, topBarManager)
        }
        composable<Screen.TrainingEquipment>{
            val viewModel = koinViewModel<TrainingEquipmentViewModel>()
            TrainingEquipmentView(viewModel, navHostController, topBarManager)
        }
        composable<Screen.TrainingEquipmentDetail> { backStackEntry ->
            val addEditTrainingEquipmentRoute: Screen.TrainingEquipmentDetail = backStackEntry.toRoute()
            val viewModel = koinViewModel<TrainingEquipmentDetailViewModel>()
            val id = if (addEditTrainingEquipmentRoute.id != null) Uuid.parse(addEditTrainingEquipmentRoute.id) else null
            TrainingEquipmentDetailView(id, viewModel, navHostController, topBarManager)
        }
        composable<Screen.Sleeps> {
            val viewModel = koinViewModel<SleepViewModel>()
            SleepOverviewView(viewModel, topBarManager)
        }
        composable<Screen.Steps> {
            val viewModel = koinViewModel<StepsViewModel>()
            StepsView(viewModel, topBarManager)
        }
        composable<Screen.BeveragesOverview> {
            val viewModel = koinViewModel<BeverageOverviewViewModel>()
            BeverageOverviewView(viewModel, topBarManager)
        }
        composable<Screen.Beverages> {
            val viewModel = koinViewModel<BeverageViewModel>()
            BeverageView(viewModel, topBarManager)
        }
        composable<Screen.BodyWeights> {
            val viewModel = koinViewModel<WeightOverviewViewModel>()
            val settingsRepository = getKoin().get<SettingsRepository>()
            WeightOverviewView(viewModel, settingsRepository, topBarManager)
        }
    }
}
