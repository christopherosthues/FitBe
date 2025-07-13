package org.darthacheron.fitbe

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_home
import org.darthacheron.fitbe.home.HomeView
import org.darthacheron.fitbe.health.beverages.BeverageViewModel
import org.darthacheron.fitbe.health.beverages.BeverageView
import org.darthacheron.fitbe.health.sleep.SleepOverviewView
import org.darthacheron.fitbe.health.sleep.SleepViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

sealed class BottomBarNavigationItem(
    val icon: DrawableResource,
    val label: String,
    val route: String
) {
    data object Home : BottomBarNavigationItem(icon = Res.drawable.ic_home, "Home", "/home")
    data object Beverage : BottomBarNavigationItem(icon = Res.drawable.ic_home, "Beverage", "/beverage")
    data object Sleep : BottomBarNavigationItem(icon = Res.drawable.ic_home, "Sleep", "/sleep")
}

val items = listOf(
    BottomBarNavigationItem.Home,
    BottomBarNavigationItem.Beverage,
    BottomBarNavigationItem.Sleep
)

@Composable
@Preview
fun App() {
    KoinApplication(application = {}) {
        MaterialTheme {
            val navHostController = rememberNavController()

            val backStackEntry = navHostController.currentBackStackEntryAsState()

            val currentRoute = backStackEntry.value?.destination?.route

            Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = item.route == currentRoute,
                            onClick = {
                                navHostController.navigate(route = item.route) {
                                    navHostController.graph.startDestinationRoute?.let {
                                        popUpTo(it) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            modifier = Modifier.height(55.dp),
                            label = {
                                Text(
                                    item.label,
                                    color = if (item.route == currentRoute) Color.Red else Color.Black
                                )
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(item.icon), contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = if (item.route == currentRoute) Color.Red else Color.Black
                                )
                            })
                    }
                }
            }) {
                padding ->
                NavHost(
                    navController = navHostController,
                    startDestination = BottomBarNavigationItem.Home.route,
                    modifier = Modifier.padding(padding)
                ) {
                    composable(route = BottomBarNavigationItem.Home.route) {
                        HomeView(
                            navHostController
                        )
                    }
                    composable(route = BottomBarNavigationItem.Beverage.route) {
                        val viewModel = koinViewModel<BeverageViewModel>()
                        BeverageView(Modifier.padding(padding), viewModel)
                    }
                    composable(route = BottomBarNavigationItem.Sleep.route) {
                        val viewModel = koinViewModel<SleepViewModel>()
                        SleepOverviewView(Modifier.padding(padding), viewModel) }
                }

            }
        }
    }
}
//fun App() {
//    KoinApplication(application = {}) {
//        MaterialTheme {
//            Scaffold(
//                modifier = Modifier.fillMaxSize(),
//                bottomBar = {
//                    BottomAppBar(modifier = Modifier.fillMaxWidth().height(55.dp))
//                })
////            NavHost(navController = rememberNavController(), startDestination = Home) {
////                composable<Home> {
////                    HomeView()
////                }
////                composable(route = "") {  }
////            }
//
//
////        var showContent by remember { mutableStateOf(false) }
////        Column(
////            modifier = Modifier
////                .safeContentPadding()
////                .fillMaxSize(),
////            horizontalAlignment = Alignment.CenterHorizontally,
////        ) {
////            Button(onClick = { showContent = !showContent }) {
////                Text("Click me!")
////            }
////            AnimatedVisibility(showContent) {
////                val greeting = remember { Greeting().greet() }
////                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
////                    Image(painterResource(Res.drawable.compose_multiplatform), null)
////                    Text("Compose: $greeting")
////                }
////            }
////        }
//        }
//    }
//}