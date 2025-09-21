package org.darthacheron.fitbe.workouts.programs


import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_program_overview
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.ui.FilterableViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource

class ProgramOverviewViewModel(topBarManager: TopBarManager) : FilterableViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_program_overview
    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard
    override val backNavigationIconVisible: Boolean?
        get() = true

}