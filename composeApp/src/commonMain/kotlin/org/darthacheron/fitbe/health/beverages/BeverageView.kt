package org.darthacheron.fitbe.health.beverages

import androidx.compose.runtime.Composable
import org.darthacheron.fitbe.health.components.HealthView
import org.darthacheron.fitbe.health.components.HealthViewModel

@Composable
fun BeverageView(
    healthViewModel: HealthViewModel,
    addBeverageDialogViewModel: AddBeverageDialogViewModel,
    beverageOverviewViewModel: BeverageOverviewViewModel,
    beverageDailyViewModel: BeverageDailyViewModel
) {
    HealthView(
        healthViewModel = healthViewModel,
        overviewView = { BeverageOverviewView(beverageOverviewViewModel, addBeverageDialogViewModel) },
        detailView = { BeverageDailyView(beverageDailyViewModel, addBeverageDialogViewModel) }
    )
}