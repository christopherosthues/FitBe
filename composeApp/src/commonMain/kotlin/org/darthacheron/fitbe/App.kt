package org.darthacheron.fitbe

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import org.darthacheron.fitbe.components.AppTheme
import org.darthacheron.fitbe.settings.SettingsViewModel
import org.darthacheron.fitbe.navigation.RootNavGraph
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = {}) {
        // Initialize settings view model (could be injected via Koin)
        val settingsViewModel = remember { SettingsViewModel() }

        // Apply theme at the root of the app
        AppTheme(themeMode = settingsViewModel.settings.themeMode) {
            MaterialTheme {
                val navHostController = rememberNavController()
                RootNavGraph(navHostController = navHostController)
            }
        }
    }
}
