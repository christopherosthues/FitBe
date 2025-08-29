package org.darthacheron.fitbe.navigation


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.darthacheron.fitbe.exercises.ExercisesView
import org.darthacheron.fitbe.exercises.ExercisesViewModel
import org.darthacheron.fitbe.exercises.TrainingEquipmentView
import org.darthacheron.fitbe.exercises.TrainingEquipmentViewModel
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
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
fun BottomBarNavGraph(navHostController: NavHostController, paddingValues: PaddingValues) {
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
                HomeView(viewModel, navHostController)
            }
            composable<Screen.Exercises> {
                val viewModel = koinViewModel<ExercisesViewModel>()
                ExercisesView(viewModel)
            }
            composable<Screen.TrainingEquipment>{
                val viewModel = koinViewModel<TrainingEquipmentViewModel>()
                TrainingEquipmentView(viewModel)
            }
            composable<Screen.Health> {
                val viewModel = koinViewModel<HealthOverviewViewModel>()
                val settingsRepository = getKoin().get<SettingsRepository>()
                HealthOverviewView(viewModel, settingsRepository, navHostController)
            }
            composable<Screen.Profile> {
                val viewModel = koinViewModel<ProfileViewModel>()
                val settingsRepository = getKoin().get<SettingsRepository>()
                ProfileView(viewModel, settingsRepository)
            }
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
    }
}

