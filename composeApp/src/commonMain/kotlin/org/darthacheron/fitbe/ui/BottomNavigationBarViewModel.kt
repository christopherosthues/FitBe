package org.darthacheron.fitbe.ui

import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_settings
import fitbe.composeapp.generated.resources.top_bar_content_description_settings
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.ui.state.TopBarAction

abstract class BottomNavigationBarViewModel(
    val topNavHostController: NavHostController,
    val navHostController: NavHostController,
    val topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    override val actions: List<TopBarAction>
        get() =
            listOf(
                TopBarAction(
                    icon = Res.drawable.ic_settings,
                    contentDescription = Res.string.top_bar_content_description_settings,
                    onClick = { topNavHostController.navigate(Screen.Settings) },
                    isVisible = true
                )
            )

    override val backNavigationIconVisible: Boolean?
        get() = false
}