package org.darthacheron.fitbe.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.darthacheron.fitbe.RootScreen
import org.darthacheron.fitbe.settings.SettingsView
import org.darthacheron.fitbe.settings.SettingsViewModel
import org.darthacheron.fitbe.ui.TopBarManager // Added import
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
fun RootNavGraph(
    navHostController: NavHostController,
    topBarManager: TopBarManager // Added parameter
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Root
    ) {
        composable<Screen.Root> {
            RootScreen(
                topBarManager = topBarManager, // Pass to RootScreen
                navigateToSettings = { navHostController.navigate(Screen.Settings) }
            )
        }
        composable<Screen.Settings> {
            val viewModel = koinViewModel<SettingsViewModel>()
            // SettingsView might also want to configure the TopAppBar
            // If so, it would also need access to topBarManager, perhaps via its ViewModel
            SettingsView(navHostController, viewModel, topBarManager)
        }
    }
}
