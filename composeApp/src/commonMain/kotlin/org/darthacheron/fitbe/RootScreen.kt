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
import fitbe.composeapp.generated.resources.ic_health
import fitbe.composeapp.generated.resources.ic_settings
import fitbe.composeapp.generated.resources.top_bar_title_beverages
import fitbe.composeapp.generated.resources.top_bar_title_beverages_overview
import fitbe.composeapp.generated.resources.top_bar_title_body_weights
import fitbe.composeapp.generated.resources.top_bar_title_exercises
import fitbe.composeapp.generated.resources.top_bar_title_health
import fitbe.composeapp.generated.resources.top_bar_title_home
import fitbe.composeapp.generated.resources.top_bar_title_profile
import fitbe.composeapp.generated.resources.top_bar_title_settings
import fitbe.composeapp.generated.resources.top_bar_title_sleeps
import fitbe.composeapp.generated.resources.top_bar_title_steps
import org.darthacheron.fitbe.navigation.BottomBarNavGraph
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.navigation.bottomBarDestinations
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RootScreen(navigateToSettings: () -> Unit) {
    val navHostController = rememberNavController()
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination by remember { derivedStateOf { backStackEntry?.destination?.route } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topBarTitle(
                            // TODO: Title
                            currentDestination?.substringAfterLast(".")?.substringBefore("/")
                                ?.substringBefore("?")
                        )
                    )
                },
                navigationIcon = {
                    AnimatedVisibility(
                        visible = !isTopBottomBarNavigation(currentDestination)
                    ) {
                        IconButton(
                            onClick = { navHostController.navigateUp() }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_back),
                                contentDescription = null
                            )
                        }
                    }
                },
                actions = {
                    AnimatedVisibility(
                        visible = isTopBottomBarNavigation(currentDestination)
                    ) {
                        IconButton(
                            onClick = navigateToSettings
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_settings),
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                bottomBarDestinations.forEach {
                    val isSelected = checkIfSelected(
                        currentDestination = currentDestination,
                        currentBottomBarItem = it.screen.toString()
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
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = if (isSelected) Color.Red else Color.Black
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        BottomBarNavGraph(navHostController = navHostController, paddingValues = paddingValues)
    }
}

@Composable
private fun topBarTitle(currentDestination: String?): String {
    return when (currentDestination) {
        Screen.Home.toString() -> stringResource(Res.string.top_bar_title_home)
        Screen.Exercises.toString() -> stringResource(Res.string.top_bar_title_exercises)
        Screen.Health.toString() -> stringResource(Res.string.top_bar_title_health)
        Screen.Profile.toString() -> stringResource(Res.string.top_bar_title_profile)
        Screen.Sleeps.toString() -> stringResource(Res.string.top_bar_title_sleeps)
        Screen.Steps.toString() -> stringResource(Res.string.top_bar_title_steps)
        Screen.Beverages.toString() -> stringResource(Res.string.top_bar_title_beverages)
        Screen.BeveragesOverview.toString() -> stringResource(Res.string.top_bar_title_beverages_overview)
        Screen.BodyWeights.toString() -> stringResource(Res.string.top_bar_title_body_weights)
        Screen.Settings.toString() -> stringResource(Res.string.top_bar_title_settings)
        else -> ""
    }
}

private fun isTopBottomBarNavigation(currentDestination: String?): Boolean {
    for (bottomNavigationItem in bottomBarDestinations) {
        if (currentDestination?.contains(bottomNavigationItem.screen.toString()) ?: false) {
            return true
        }
    }

    return false
}

private fun checkIfSelected(
    currentDestination: String?,
    currentBottomBarItem: String
): Boolean {
    return if (currentDestination?.contains(currentBottomBarItem) == true) true
    else if ((currentDestination?.contains(Screen.Beverages.toString()) == true ||
                currentDestination?.contains(Screen.BeveragesOverview.toString()) == true ||
                currentDestination?.contains(Screen.Sleeps.toString()) == true ||
                currentDestination?.contains(Screen.Steps.toString()) == true ||
                currentDestination?.contains(Screen.BodyWeights.toString()) == true) &&
        currentBottomBarItem == Screen.Health.toString()
    ) true
    else false
}