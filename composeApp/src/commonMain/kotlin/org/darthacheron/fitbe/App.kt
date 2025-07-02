package org.darthacheron.fitbe

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.darthacheron.fitbe.home.Home
import org.jetbrains.compose.ui.tooling.preview.Preview

import org.darthacheron.fitbe.home.HomeView
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = {}) {
        MaterialTheme {
            NavHost(navController = rememberNavController(), startDestination = Home) {
                composable<Home> {
                    HomeView()
                }
                composable(route = "") {  }
            }
//        var showContent by remember { mutableStateOf(false) }
//        Column(
//            modifier = Modifier
//                .safeContentPadding()
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
        }
    }
}