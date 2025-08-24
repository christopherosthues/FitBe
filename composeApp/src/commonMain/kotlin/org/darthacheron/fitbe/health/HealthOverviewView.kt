package org.darthacheron.fitbe.health


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.darthacheron.fitbe.health.beverages.BeverageOverviewViewModel
import org.darthacheron.fitbe.health.beverages.PlotBeverages
import org.darthacheron.fitbe.health.steps.PlotSteps
import org.darthacheron.fitbe.health.steps.StepsViewModel
import org.darthacheron.fitbe.health.weight.PlotBodyWeights
import org.darthacheron.fitbe.health.weight.WeightOverviewViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
fun HealthOverviewView(
    healthOverviewViewModel: HealthOverviewViewModel,
    settingsRepository: SettingsRepository,
    navHostController: NavHostController
) {
    val settings by settingsRepository.getSettingsFlow().collectAsState(Settings())

    Column {
        BoxWithConstraints(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            LazyColumn {
                item {
                    Thumbnail(
                        onClick = { navHostController.navigate(Screen.BeveragesOverview) },
                        content = {
                            BeveragesOverview(
                                healthOverviewViewModel.beverageOverviewViewModel,
                            )
                        }
                    )
                }
                item {
                    TextButton(
                        onClick = { navHostController.navigate(Screen.Sleeps) }
                    ) {
                        Text(text = "Sleeps")
                    }
                }
                item {
                    Thumbnail(
                        onClick = { navHostController.navigate(Screen.Steps) },
                        content = {
                            StepsOverview(
                                healthOverviewViewModel.stepsViewModel
                            )
                        }
                    )
                }
                item {
                    Thumbnail(
                        onClick = { navHostController.navigate(Screen.BodyWeights) },
                        content = {
                            BodyWeightOverView(
                                healthOverviewViewModel.bodyWeightOverviewViewModel,
                                settings
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Thumbnail(onClick: () -> Unit, content: @Composable () -> Unit) {
    Surface(
        shadowElevation = 2.dp,
        modifier = Modifier.padding(16.dp).height(250.dp).clickable(onClick = onClick),
        content = content
    )
}

@Composable
private fun BeveragesOverview(beverageOverviewViewModel: BeverageOverviewViewModel) {
    val beveragesOverview by beverageOverviewViewModel.beverages.collectAsState()
    val maxBeverages by beverageOverviewViewModel.maxBeverages.collectAsState()
    val dateRange by beverageOverviewViewModel.dateRangeFlow.collectAsState()
    val dates = beverageOverviewViewModel.dates(beveragesOverview)

    PlotBeverages(
        beverages = beveragesOverview,
        dateRange = dateRange,
        dates = dates,
        maxBeverages = maxBeverages,
        thumbnail = true,
    )
}

@Composable
private fun StepsOverview(
    stepsViewModel: StepsViewModel,
) {
    val steps by stepsViewModel.steps.collectAsState()
    val maxBodyWeight by stepsViewModel.maxSteps.collectAsState()
    val dateRange by stepsViewModel.dateRangeFlow.collectAsState()
    val dates = stepsViewModel.dates(steps)

    PlotSteps(
        stepsData = steps,
        dateRange = dateRange,
        dates = dates,
        maxSteps = maxBodyWeight,
        thumbnail = true,
    )
}

@Composable
private fun BodyWeightOverView(
    bodyWeightOverviewViewModel: WeightOverviewViewModel,
    settings: Settings,
) {
    val bodyWeights by bodyWeightOverviewViewModel.bodyWeights.collectAsState()
    val maxBodyWeight by bodyWeightOverviewViewModel.maxWeight.collectAsState()
    val dateRange by bodyWeightOverviewViewModel.dateRangeFlow.collectAsState()
    val dates = bodyWeightOverviewViewModel.dates(bodyWeights)

    PlotBodyWeights(
        bodyWeights = bodyWeights,
        dateRange = dateRange,
        dates = dates,
        settings = settings,
        maxWeight = maxBodyWeight,
        thumbnail = true,
    )
}
