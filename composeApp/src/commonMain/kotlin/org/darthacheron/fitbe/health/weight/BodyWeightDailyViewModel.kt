package org.darthacheron.fitbe.health.weight

import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource

class BodyWeightDailyViewModel(
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    override val title: StringResource
        get() = TODO("Not yet implemented")

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    override val backNavigationIconVisible: Boolean?
        get() = true
}