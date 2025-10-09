package org.darthacheron.fitbe.health.beverages

import androidx.compose.runtime.Composable
import org.darthacheron.fitbe.health.componenets.HealthView
import org.darthacheron.fitbe.health.componenets.HealthViewModel

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