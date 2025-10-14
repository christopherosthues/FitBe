package org.darthacheron.fitbe.health

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.health.beverages.BeverageOverviewViewModel
import org.darthacheron.fitbe.health.beverages.PlotBeverages
import org.darthacheron.fitbe.health.sleep.PlotSleeps
import org.darthacheron.fitbe.health.sleep.SleepOverviewViewModel
import org.darthacheron.fitbe.health.steps.PlotSteps
import org.darthacheron.fitbe.health.steps.StepsOverviewViewModel
import org.darthacheron.fitbe.health.weight.PlotBodyWeights
import org.darthacheron.fitbe.health.weight.WeightOverviewViewModel
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
fun HealthOverviewView(
    healthOverviewViewModel: HealthOverviewViewModel,
    settingsRepository: SettingsRepository
) {
    LaunchedEffect(Unit) {
        healthOverviewViewModel.updateTopBarConfig()
    }
    val settings by settingsRepository.getSettingsFlow().collectAsState(Settings())

    Column {
        BoxWithConstraints(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            LazyColumn {
                item {
                    Thumbnail(
                        onClick = { healthOverviewViewModel.navigateToBeverageOverview() },
                        content = {
                            BeveragesOverview(healthOverviewViewModel.beverageOverviewViewModel)
                        }
                    )
                }
                item {
                    Thumbnail(
                        onClick = { healthOverviewViewModel.navigateToSleepOverview() },
                        content = {
                            SleepsOverview(healthOverviewViewModel.sleepOverviewViewModel)
                        }
                    )
                }
                item {
                    Thumbnail(
                        onClick = { healthOverviewViewModel.navigateToStepsOverview() },
                        content = {
                            StepsOverview(
                                healthOverviewViewModel.stepsOverviewViewModel
                            )
                        }
                    )
                }
                item {
                    Thumbnail(
                        onClick = { healthOverviewViewModel.navigateToBodyWeightOverview() },
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
private fun Thumbnail(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        shadowElevation = 2.dp,
        modifier = Modifier.padding(16.dp).height(250.dp).clickable(onClick = onClick),
        content = content
    )
}

@Composable
private fun BeveragesOverview(beverageOverviewViewModel: BeverageOverviewViewModel) {
    val uiState by beverageOverviewViewModel.uiState.collectAsState()
    val maxBeverages by beverageOverviewViewModel.maxBeverages.collectAsState()
    val dateRange by beverageOverviewViewModel.dateRangeFlow.collectAsState()

    PlotBeverages(
        beverages = uiState.beverages,
        dateRange = dateRange,
        dates = uiState.dates,
        maxBeverages = maxBeverages,
        thumbnail = true
    )
}

@Composable
private fun StepsOverview(stepsOverviewViewModel: StepsOverviewViewModel) {
    val uiState by stepsOverviewViewModel.uiState.collectAsState()
    val maxSteps by stepsOverviewViewModel.maxSteps.collectAsState()
    val dateRange by stepsOverviewViewModel.dateRangeFlow.collectAsState()

    PlotSteps(
        stepsData = uiState.steps,
        dateRange = dateRange,
        dates = uiState.dates,
        maxSteps = maxSteps,
        thumbnail = true
    )
}

@Composable
private fun SleepsOverview(sleepsViewModel: SleepOverviewViewModel) {
    val uiState by sleepsViewModel.uiState.collectAsState()
    val maxSleeps by sleepsViewModel.maxSleeps.collectAsState()
    val dateRange by sleepsViewModel.dateRangeFlow.collectAsState()

    PlotSleeps(
        sleeps = uiState.sleeps,
        dateRange = dateRange,
        dates = uiState.dates,
        maxSleeps = maxSleeps,
        thumbnail = true
    )
}

@Composable
private fun BodyWeightOverView(
    bodyWeightOverviewViewModel: WeightOverviewViewModel,
    settings: Settings
) {
    val uiState by bodyWeightOverviewViewModel.uiState.collectAsState()
    val maxBodyWeight by bodyWeightOverviewViewModel.maxWeight.collectAsState()
    val dateRange by bodyWeightOverviewViewModel.dateRangeFlow.collectAsState()

    PlotBodyWeights(
        bodyWeights = uiState.bodyWeights,
        dateRange = dateRange,
        dates = uiState.dates,
        weightUnit = settings.weightUnit,
        maxWeight = maxBodyWeight,
        thumbnail = true
    )
}