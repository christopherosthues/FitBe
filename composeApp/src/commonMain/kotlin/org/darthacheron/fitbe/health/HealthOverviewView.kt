package org.darthacheron.fitbe.health


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.darthacheron.fitbe.navigation.Screen
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
            onClick = { navHostController.navigate(Screen.Beverages) }
        ) {
            Text(text = "Beverages")
        }
        TextButton(
            onClick = { navHostController.navigate(Screen.Sleeps) }
        ) {
            Text(text = "Sleeps")
        }
    }
}

