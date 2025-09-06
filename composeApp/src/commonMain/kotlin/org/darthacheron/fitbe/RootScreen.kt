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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_back
import org.darthacheron.fitbe.navigation.BottomBarNavGraph
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.navigation.bottomBarDestinations
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.ui.state.TopBarConfig
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootScreen(
    topNavHostController: NavHostController,
    topBarManager: TopBarManager,
) {
    val navHostController = rememberNavController()
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute by remember { derivedStateOf { backStackEntry?.destination?.route } }

    val topBarConfig by topBarManager.topBarConfigFlow.collectAsState(initial = TopBarConfig())

    val currentDestinationRoute = currentRoute?.substringAfterLast(".")?.substringBefore("/")
        ?.substringBefore("?")
    val isMainBottomBarDestination = remember(currentRoute) {
        bottomBarDestinations.any { currentDestinationRoute?.equals(it.screen.toString()) == true }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val titleResource = topBarConfig.title
                    val title = if (titleResource != null) stringResource(titleResource) else currentDestinationRoute ?: ""
                    Text(text = title)
                },
                navigationIcon = {
                    val navIconVisible = topBarConfig.backNavigationIconVisible ?: !isMainBottomBarDestination
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
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                bottomBarDestinations.forEach {
                    val isSelected = topBarConfig.bottomBarSelected == it.screen
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
            topNavHostController = topNavHostController,
            navHostController = navHostController,
            paddingValues = paddingValues,
        )
    }
}
