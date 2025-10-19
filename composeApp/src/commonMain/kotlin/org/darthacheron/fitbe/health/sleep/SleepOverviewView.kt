package org.darthacheron.fitbe.health.sleep

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.health.components.OverviewView
import org.darthacheron.fitbe.health.sleep.manage.AddSleepDialog
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun SleepOverviewView(
    viewModel: SleepOverviewViewModel = koinViewModel()
) {
    OverviewView(
        overviewViewModel = viewModel,
        plot = { state, dateRange ->
            PlotSleeps(
                modifier = Modifier.padding(top = 8.dp, bottom = 64.dp),
                sleeps = state.sleeps,
                dateRange = dateRange,
                dates = state.dates,
                maxSleeps = state.maxSleeps,
                thumbnail = false,
                targetSleepDuration = state.target
            )
        },
        addDialog = { dismissDialog ->
            AddSleepDialog(
                date = null,
                onSave = { start, end ->
                    viewModel.addSleep(start, end)
                    dismissDialog()
                },
                onDismiss = { dismissDialog() }
            )
        }
    )
}