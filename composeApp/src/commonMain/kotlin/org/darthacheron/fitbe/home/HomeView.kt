package org.darthacheron.fitbe.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.serialization.Serializable
import org.darthacheron.fitbe.dependencies.MyViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object Home

@Composable
@Preview
fun HomeView(navController: NavController) {
    val viewModel = koinViewModel<MyViewModel>()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = viewModel.getHelloWorldString())
    }
}