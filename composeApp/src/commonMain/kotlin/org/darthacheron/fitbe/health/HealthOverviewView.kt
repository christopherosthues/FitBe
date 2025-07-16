package org.darthacheron.fitbe.health


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import org.darthacheron.fitbe.NavigationRoutes
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun HealthOverviewView(
    healthOverviewViewModel: HealthOverviewViewModel,
    navHostController: NavHostController
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TextButton(
            onClick = { navHostController.navigate(NavigationRoutes.Beverages) }
        ) {
            Text(text = "Beverages")
        }
        TextButton(
            onClick = { navHostController.navigate(NavigationRoutes.Sleeps) }
        ) {
            Text(text = "Sleeps")
        }
    }
}

