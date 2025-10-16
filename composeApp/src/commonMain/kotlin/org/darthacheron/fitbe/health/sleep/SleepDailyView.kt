package org.darthacheron.fitbe.health.sleep

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.darthacheron.fitbe.health.components.DailyView
import org.darthacheron.fitbe.health.weight.BodyWeightDailyUiState
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun SleepDailyView(
    sleepDailyViewModel: SleepDailyViewModel,
    addSleepDialogViewModel: AddSleepDialogViewModel
) {
    DailyView(
        dailyViewModel = sleepDailyViewModel,
        detailView = { state, date ->
            val maxSleep by sleepDailyViewModel.maxSleeps.collectAsState()
            val targetSleepDuration by sleepDailyViewModel.targetSleeps.collectAsState()
            SleepDetailView(
                state = state,
                maxSleep = maxSleep,
                targetSleepDuration = targetSleepDuration
            )
        },
        addDialog = { onDismiss ->
            AddSleepDialog(
                viewModel = addSleepDialogViewModel,
                onSave = { start, end ->
                    sleepDailyViewModel.addSleep(start, end)
                    onDismiss()
                },
                onDismiss = { onDismiss() }
            )
        }
    )
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun SleepDetailView(
    state: SleepDailyUiState,
    maxSleep: Int,
    targetSleepDuration: Int
) {

}