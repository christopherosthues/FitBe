package org.darthacheron.fitbe.health.sleep

import androidx.compose.runtime.Composable
import org.darthacheron.fitbe.health.componenets.HealthView
import org.darthacheron.fitbe.health.componenets.HealthViewModel

@Composable
fun SleepView(
    healthViewModel: HealthViewModel,
    addSleepDialogViewModel: AddSleepDialogViewModel,
    sleepOverviewViewModel: SleepOverviewViewModel,
//    sleepDayViewModel: SleepDayViewModel
) {
    HealthView(
        healthViewModel = healthViewModel,
        overviewView = { SleepOverviewView(sleepOverviewViewModel, addSleepDialogViewModel) },
        detailView = { },
    )
}