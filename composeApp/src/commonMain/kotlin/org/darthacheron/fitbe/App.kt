package org.darthacheron.fitbe

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.darthacheron.fitbe.navigation.RootNavGraph
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = {}) {
        MaterialTheme {
            val navHostController = rememberNavController()
            RootNavGraph(navHostController = navHostController)
        }
    }
}
