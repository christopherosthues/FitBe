package org.darthacheron.fitbe.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun HomeView(
    homeViewModel: HomeViewModel
) {
    LaunchedEffect(Unit) {
        homeViewModel.updateTopBarConfig()
    }
    Column() {
        // Your HomeView content here
    }
}
