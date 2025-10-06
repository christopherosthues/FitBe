package org.darthacheron.fitbe.health.sleep


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SleepDailyView(
    sleepDailyViewModel: SleepDailyViewModel,
    addSleepDialogViewModel: AddSleepDialogViewModel
) {
    LaunchedEffect(Unit) {
        sleepDailyViewModel.updateTopBarConfig()
    }
}

