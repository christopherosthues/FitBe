package org.darthacheron.fitbe.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.darthacheron.fitbe.RootScreen
import org.darthacheron.fitbe.settings.SettingsView
import org.darthacheron.fitbe.settings.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
fun RootNavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Root
    ) {
        composable<Screen.Root> {
            RootScreen(navigateToSettings = { navHostController.navigate(Screen.Settings) })
        }
        composable<Screen.Settings> {
            val viewModel = koinViewModel<SettingsViewModel>()
            SettingsView(navHostController, viewModel)
        }
    }
}

