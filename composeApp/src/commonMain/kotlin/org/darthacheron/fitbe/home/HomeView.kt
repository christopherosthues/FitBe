package org.darthacheron.fitbe.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_home
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.ui.state.TopBarConfig
import org.jetbrains.compose.ui.tooling.preview.Preview

// Home object is not used directly in this file for TopBar config, kept for context if used elsewhere.
// import kotlinx.serialization.Serializable
// @Serializable
// object Home

@Composable
// @Preview // Preview might be complex with TopBarManager injection
fun HomeView(
    homeViewModel: HomeViewModel, 
    navController: NavController, 
    topBarManager: TopBarManager // Added parameter
) {
    DisposableEffect(Unit) {
        topBarManager.setConfig(
            TopBarConfig(
                title = Res.string.top_bar_title_home,
                actions = emptyList() // No specific actions for home, settings icon will be shown by RootScreen
            )
        )
        onDispose {
            topBarManager.resetConfig() // Reset when leaving HomeView
        }
    }

    Column() {
        // Your HomeView content here
    }
}
