package org.darthacheron.fitbe

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import io.github.vinceglb.filekit.coil.addPlatformFileSupport
import org.darthacheron.fitbe.navigation.RootNavGraph
import org.darthacheron.fitbe.settings.SettingsViewModel
import org.darthacheron.fitbe.ui.AppTheme
import org.darthacheron.fitbe.ui.TopBarManager
import org.koin.compose.KoinApplication
import org.koin.compose.getKoin
import org.koin.compose.koinInject

@Composable
@Preview
fun App(
    settingsViewModel: SettingsViewModel = koinInject(),
    startUpService: StartUpService = koinInject(),
    topBarManager: TopBarManager = koinInject()
) {
    KoinApplication(application = {}) {
        val settings = settingsViewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            startUpService.initialize()
        }
        setSingletonImageLoaderFactory { context ->
            ImageLoader
                .Builder(context)
                .components {
                    addPlatformFileSupport()
                }.build()
        }

        AppTheme(themeMode = settings.value.persistedSettings.themeMode) {
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