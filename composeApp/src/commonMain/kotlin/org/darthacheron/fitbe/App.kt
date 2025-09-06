package org.darthacheron.fitbe

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import io.github.vinceglb.filekit.coil.addPlatformFileSupport
import org.darthacheron.fitbe.components.AppTheme
import org.darthacheron.fitbe.navigation.RootNavGraph
import org.darthacheron.fitbe.settings.SettingsViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.getKoin

@Composable
@Preview
fun App() {
    KoinApplication(application = {}) {
        val koin = getKoin()
        val settingsViewModel = remember { koin.get<SettingsViewModel>() }
        val startUpService = remember { koin.get<StartUpService>() }
        val topBarManager = remember { koin.get<TopBarManager>() }

        LaunchedEffect(Unit) {
            startUpService.initialize()
        }
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .components {
                    addPlatformFileSupport()
                }
                .build()
        }

        // Apply theme at the root of the app
        AppTheme(themeMode = settingsViewModel.currentSettings.themeMode) {
            MaterialTheme {
                val navHostController = rememberNavController()
                RootNavGraph(
                    navHostController = navHostController,
                    topBarManager = topBarManager
                )
            }
        }
    }
}
