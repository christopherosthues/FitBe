package org.darthacheron.fitbe.workouts.templates


import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_workout_template_detail
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource

class WorkoutTemplateDetailViewModel(
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_workout_template_detail

    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard
}