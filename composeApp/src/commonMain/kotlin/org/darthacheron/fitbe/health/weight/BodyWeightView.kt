package org.darthacheron.fitbe.health.weight

import androidx.compose.runtime.Composable
import org.darthacheron.fitbe.health.components.HealthView
import org.darthacheron.fitbe.health.components.HealthViewModel

@Composable
fun BodyWeightView(
    healthViewModel: HealthViewModel,
    addBodyWeightDialogViewModel: AddBodyWeightDialogViewModel,
    bodyWeightOverviewViewModel: WeightOverviewViewModel,
    bodyWeightDailyViewModel: BodyWeightDailyViewModel
) {
    HealthView(
        healthViewModel = healthViewModel,
        overviewView = { WeightOverviewView(bodyWeightOverviewViewModel, addBodyWeightDialogViewModel) },
        detailView = { BodyWeightDailyView(bodyWeightDailyViewModel, addBodyWeightDialogViewModel) }
    )
}