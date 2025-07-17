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
import org.darthacheron.fitbe.health.HealthOverviewView
import org.darthacheron.fitbe.health.HealthOverviewViewModel
import org.darthacheron.fitbe.health.beverages.BeverageView
import org.darthacheron.fitbe.health.beverages.BeverageViewModel
import org.darthacheron.fitbe.health.sleep.SleepOverviewView
import org.darthacheron.fitbe.health.sleep.SleepViewModel
import org.darthacheron.fitbe.home.HomeView
import org.darthacheron.fitbe.profile.ProfileView
import org.darthacheron.fitbe.profile.ProfileViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
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
                HomeView(navHostController)
            }
            composable<Screen.Exercises> {
                val viewModel = koinViewModel<ExercisesViewModel>()
                ExercisesView(viewModel)
            }
            composable<Screen.Health> {
                val viewModel = koinViewModel<HealthOverviewViewModel>()
                HealthOverviewView(viewModel, navHostController)
            }
            composable<Screen.Profile> {
                val viewModel = koinViewModel<ProfileViewModel>()
                ProfileView(viewModel)
            }
        }
        composable<Screen.Sleeps> {
            val viewModel = koinViewModel<SleepViewModel>()
            SleepOverviewView(viewModel)
        }
        composable<Screen.Beverages> {
            val viewModel = koinViewModel<BeverageViewModel>()
            BeverageView(viewModel)
        }
    }
}

