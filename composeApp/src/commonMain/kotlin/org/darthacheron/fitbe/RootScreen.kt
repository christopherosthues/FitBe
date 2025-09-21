package org.darthacheron.fitbe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_back
import org.darthacheron.fitbe.navigation.BottomBarNavGraph
import org.darthacheron.fitbe.navigation.bottomBarDestinations
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.ui.state.TopBarConfig
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootScreen(
    topNavHostController: NavHostController,
    bottomBarNavController: NavHostController,
    topBarManager: TopBarManager,
) {
    val backStackEntry by bottomBarNavController.currentBackStackEntryAsState()
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                navigationIcon = {
                    val navIconVisible = topBarConfig.backNavigationIconVisible ?: !isMainBottomBarDestination
                    AnimatedVisibility(visible = navIconVisible) {
                        IconButton(onClick = { bottomBarNavController.navigateUp() }) {
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
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                bottomBarDestinations.forEach { bottomBarDestination ->
                    val isSelected = topBarConfig.bottomBarSelected == bottomBarDestination.screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            bottomBarNavController.navigate(bottomBarDestination.screen) {
                                popUpTo(bottomBarNavController.graph.findStartDestination().route!!) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.height(55.dp),
                        label = {
                            Text(
                                text = stringResource(bottomBarDestination.label),
                            )
                        },
                        icon = {
                            Icon(
                                painter = painterResource(bottomBarDestination.icon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        BottomBarNavGraph(
            topNavHostController = topNavHostController,
            bottomBarNavHostController = bottomBarNavController,
            paddingValues = paddingValues,
        )
    }
}
