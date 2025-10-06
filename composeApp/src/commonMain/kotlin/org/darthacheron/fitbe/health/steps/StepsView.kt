package org.darthacheron.fitbe.health.steps

import androidx.compose.runtime.Composable
import org.darthacheron.fitbe.health.componenets.HealthView
import org.darthacheron.fitbe.health.componenets.HealthViewModel

@Composable
fun StepsView(
    healthViewModel: HealthViewModel,
    addStepsDialogViewModel: AddStepsDialogViewModel,
    stepsOverviewViewModel: StepsOverviewViewModel,
    stepsDailyViewModel: StepsDailyViewModel,
) {
    HealthView(
        healthViewModel = healthViewModel,
        overviewView = { StepsOverviewView(stepsOverviewViewModel, addStepsDialogViewModel) },
        detailView = { StepsDailyView(stepsDailyViewModel, addStepsDialogViewModel) },
    )
}
