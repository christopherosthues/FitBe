package org.darthacheron.fitbe.health.weight

import androidx.compose.runtime.Composable
import org.darthacheron.fitbe.health.components.HealthView
import org.darthacheron.fitbe.health.components.HealthViewModel

@Composable
fun BodyWeightView(
    healthViewModel: HealthViewModel,
    addBodyWeightDialogViewModel: AddBodyWeightDialogViewModel,
    bodyWeightOverviewViewModel: BodyWeightOverviewViewModel,
    bodyWeightDailyViewModel: BodyWeightDailyViewModel
) {
    HealthView(
        healthViewModel = healthViewModel,
        overviewView = { BodyWeightOverviewView(bodyWeightOverviewViewModel, addBodyWeightDialogViewModel) },
        detailView = { BodyWeightDailyView(bodyWeightDailyViewModel, addBodyWeightDialogViewModel) }
    )
}