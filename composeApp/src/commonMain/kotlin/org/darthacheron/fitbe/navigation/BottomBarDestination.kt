package org.darthacheron.fitbe.navigation

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.bottom_bar_content_description_exercises
import fitbe.composeapp.generated.resources.bottom_bar_content_description_health
import fitbe.composeapp.generated.resources.bottom_bar_content_description_home
import fitbe.composeapp.generated.resources.bottom_bar_content_description_profile
import fitbe.composeapp.generated.resources.bottom_bar_exercises
import fitbe.composeapp.generated.resources.bottom_bar_health
import fitbe.composeapp.generated.resources.bottom_bar_home
import fitbe.composeapp.generated.resources.bottom_bar_profile
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_exercise
import fitbe.composeapp.generated.resources.ic_health
import fitbe.composeapp.generated.resources.ic_home
import fitbe.composeapp.generated.resources.ic_profile
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

sealed class BottomBarDestination(
    val screen: Screen,
    val label: StringResource,
    val icon: DrawableResource,
    val contentDescription: StringResource
) {
    data object Home : BottomBarDestination(
        screen = Screen.Home,
        label = Res.string.bottom_bar_home,
        icon = Res.drawable.ic_home,
        contentDescription = Res.string.bottom_bar_content_description_home
    )

    data object ExercisesDashboard : BottomBarDestination(
        screen = Screen.ExercisesDashboard,
        label = Res.string.bottom_bar_exercises,
        icon = Res.drawable.ic_exercise,
        contentDescription = Res.string.bottom_bar_content_description_exercises
    )

    data object Health : BottomBarDestination(
        screen = Screen.Health,
        label = Res.string.bottom_bar_health,
        icon = Res.drawable.ic_health,
        contentDescription = Res.string.bottom_bar_content_description_health
    )

    data object Profile : BottomBarDestination(
        screen = Screen.Profile,
        label = Res.string.bottom_bar_profile,
        icon = Res.drawable.ic_profile,
        contentDescription = Res.string.bottom_bar_content_description_profile
    )
}

val bottomBarDestinations =
    listOf(
        BottomBarDestination.Home,
        BottomBarDestination.ExercisesDashboard,
        BottomBarDestination.Health,
        BottomBarDestination.Profile
    )