package org.darthacheron.fitbe.health.sleep

import androidx.compose.runtime.Composable
import org.darthacheron.fitbe.health.components.HealthView
import org.darthacheron.fitbe.health.components.HealthViewModel

@Composable
fun SleepView(
    healthViewModel: HealthViewModel,
    addSleepDialogViewModel: AddSleepDialogViewModel,
    sleepOverviewViewModel: SleepOverviewViewModel,
    sleepDailyViewModel: SleepDailyViewModel
) {
    HealthView(
        healthViewModel = healthViewModel,
        overviewView = { SleepOverviewView(sleepOverviewViewModel, addSleepDialogViewModel) },
        detailView = { SleepDailyView(sleepDailyViewModel, addSleepDialogViewModel) }
    )
}