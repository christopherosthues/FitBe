package org.darthacheron.fitbe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_back
import fitbe.composeapp.generated.resources.ic_settings
import fitbe.composeapp.generated.resources.top_bar_title_beverages
import fitbe.composeapp.generated.resources.top_bar_title_beverages_overview
import fitbe.composeapp.generated.resources.top_bar_title_body_weights
import fitbe.composeapp.generated.resources.top_bar_title_exercises
import fitbe.composeapp.generated.resources.top_bar_title_exercises_dashboard
import fitbe.composeapp.generated.resources.top_bar_title_health
import fitbe.composeapp.generated.resources.top_bar_title_home
import fitbe.composeapp.generated.resources.top_bar_title_profile
import fitbe.composeapp.generated.resources.top_bar_title_settings
import fitbe.composeapp.generated.resources.top_bar_title_sleeps
import fitbe.composeapp.generated.resources.top_bar_title_steps
import fitbe.composeapp.generated.resources.top_bar_title_training_equipment
import org.darthacheron.fitbe.navigation.BottomBarNavGraph
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.navigation.bottomBarDestinations
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.ui.state.TopBarConfig
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
// @Preview // Preview might be complex with TopBarManager injection, can be added later if needed with a mock
@Composable
fun RootScreen(
    topBarManager: TopBarManager, // Injected
    navigateToSettings: () -> Unit
) {
    val navHostController = rememberNavController()
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute by remember { derivedStateOf { backStackEntry?.destination?.route } }

    val topBarConfig by topBarManager.topBarConfigFlow.collectAsState(initial = TopBarConfig())

    val currentDestinationRoute = currentRoute?.substringAfterLast(".")?.substringBefore("/")
        ?.substringBefore("?")
    // Determine if the current route is a main bottom bar destination
    val isMainBottomBarDestination = remember(currentRoute) {
        bottomBarDestinations.any { currentDestinationRoute?.endsWith(it.screen.toString()) == true }
    }

    println("Main bottom: $isMainBottomBarDestination")
    println("Current route: $currentRoute")
    println("Routes: ${bottomBarDestinations.map { it.screen.toString() }}")

    val defaultTitle = getDefaultTopBarTitle(currentDestinationRoute)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val titleRes = topBarConfig.title ?: defaultTitle
                    titleRes?.let { Text(text = stringResource(it)) }
                },
                navigationIcon = {
                    val navIconVisible = topBarConfig.navigationIconVisible ?: !isMainBottomBarDestination
                    AnimatedVisibility(visible = navIconVisible) {
                        IconButton(onClick = { navHostController.navigateUp() }) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_back),
                                contentDescription = null // stringResource(Res.string.ic_back) // Accessibility
                            )
                        }
                    }
                },
                actions = {
                    if (topBarConfig.actions.isNotEmpty()) {
                        topBarConfig.actions.forEach { action ->
                            AnimatedVisibility(visible = action.isVisible) {
                                IconButton(onClick = action.onClick) {
                                    Icon(
                                        painter = painterResource(action.icon),
                                        contentDescription = action.contentDescription?.let { stringResource(it) }
                                    )
                                }
                            }
                        }
                    } else if (isMainBottomBarDestination) {
                        // Default settings icon for main bottom bar destinations if no other actions are specified
                        AnimatedVisibility(visible = true) {
                            IconButton(onClick = navigateToSettings) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_settings),
                                    contentDescription = null //stringResource(Res.string.ic_settings) // Accessibility
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                bottomBarDestinations.forEach {
                    val isSelected = checkIfSelected(
                        currentDestinationRoute = currentRoute,
                        currentBottomBarItemRoute = it.screen.toString()
                    )
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navHostController.navigate(it.screen) {
                                popUpTo(navHostController.graph.findStartDestination().route!!) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.height(55.dp),
                        label = {
                            Text(
                                text = stringResource(it.label),
                                color = if (isSelected) Color.Red else Color.Black
                            )
                        },
                        icon = {
                            Icon(
                                painter = painterResource(it.icon),
                                contentDescription = null, // Label is descriptive enough
                                modifier = Modifier.size(24.dp),
                                tint = if (isSelected) Color.Red else Color.Black
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        BottomBarNavGraph(
            navHostController = navHostController,
            paddingValues = paddingValues,
            topBarManager = topBarManager // Pass TopBarManager down
        )
    }
}

@Composable
private fun getDefaultTopBarTitle(currentRoute: String?): org.jetbrains.compose.resources.StringResource? {
    return when (currentRoute) {
        Screen.Home.toString().substringAfterLast(".") -> Res.string.top_bar_title_home
        Screen.ExercisesDashboard.toString().substringAfterLast(".") -> Res.string.top_bar_title_exercises_dashboard
        Screen.Exercises.toString().substringAfterLast(".") -> Res.string.top_bar_title_exercises
        Screen.ExerciseDetail::class.simpleName!! -> Res.string.top_bar_title_exercises // Simplified
        Screen.TrainingEquipment.toString().substringAfterLast(".") -> Res.string.top_bar_title_training_equipment
        Screen.TrainingEquipmentDetail::class.simpleName!! -> Res.string.top_bar_title_training_equipment // Simplified
        Screen.Health.toString().substringAfterLast(".") -> Res.string.top_bar_title_health
        Screen.Profile.toString().substringAfterLast(".") -> Res.string.top_bar_title_profile
        Screen.Sleeps.toString().substringAfterLast(".") -> Res.string.top_bar_title_sleeps
        Screen.Steps.toString().substringAfterLast(".") -> Res.string.top_bar_title_steps
        Screen.Beverages.toString().substringAfterLast(".") -> Res.string.top_bar_title_beverages
        Screen.BeveragesOverview.toString().substringAfterLast(".") -> Res.string.top_bar_title_beverages_overview
        Screen.BodyWeights.toString().substringAfterLast(".") -> Res.string.top_bar_title_body_weights
        Screen.Settings.toString().substringAfterLast(".") -> Res.string.top_bar_title_settings
        else -> null // Let it be blank or handled by screen-specific config
    }
}

private fun checkIfSelected(
    currentDestinationRoute: String?,
    currentBottomBarItemRoute: String // This is the route of the BottomBarDestination's screen
): Boolean {
    // TODO: last . and first before /
    println("Current destination: $currentDestinationRoute")
    if (currentDestinationRoute == null) return false

    // Direct match
    if (currentDestinationRoute.endsWith(currentBottomBarItemRoute)) return true

    // Handle parent-child relationships for selection
    val healthScreens = listOf(
        Screen.Beverages.toString(), Screen.BeveragesOverview.toString(), Screen.Sleeps.toString(),
        Screen.Steps.toString(), Screen.BodyWeights.toString()
    )
    val exercisesDashboardScreens = listOf(
        Screen.TrainingEquipment.toString(), Screen.TrainingEquipmentDetail::class.simpleName!!,
        Screen.Exercises.toString(), Screen.ExerciseDetail::class.simpleName!!
    )

    if (currentBottomBarItemRoute == Screen.Health.toString() && healthScreens.any { currentDestinationRoute.endsWith(it) }) {
        return true
    }
    if (currentBottomBarItemRoute == Screen.ExercisesDashboard.toString() && exercisesDashboardScreens.any { currentDestinationRoute.endsWith(it) }) {
        return true
    }

    return false
}
