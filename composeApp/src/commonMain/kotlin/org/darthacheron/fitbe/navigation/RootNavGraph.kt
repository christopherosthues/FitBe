package org.darthacheron.fitbe.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    topBarManager: TopBarManager
) {
    val bottomBarNavController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = Screen.Root
    ) {
        composable<Screen.Root> {
            RootScreen(
                topNavHostController = navHostController,
                bottomBarNavController = bottomBarNavController,
                topBarManager = topBarManager,
            )
        }
        composable<Screen.Settings> {
            val viewModel = koinViewModel<SettingsViewModel>()
            SettingsView(navHostController, viewModel)
        }
    }
}
