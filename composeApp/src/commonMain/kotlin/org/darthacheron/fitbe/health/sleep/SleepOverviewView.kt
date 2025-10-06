package org.darthacheron.fitbe.health.sleep

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.health.OverviewView
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun SleepOverviewView(
    viewModel: SleepOverviewViewModel,
    dialogViewModel: AddSleepDialogViewModel
) {
    OverviewView(
        overviewViewModel = viewModel,
        plot = { state, dateRange ->
            val targetSleeps by viewModel.targetSleeps.collectAsState()
            val maxSleeps by viewModel.maxSleeps.collectAsState()
            PlotSleeps(
                Modifier.padding(top = 8.dp, bottom = 64.dp),
                state.sleeps,
                dateRange,
                state.dates,
                maxSleeps,
                false,
                targetSleeps,
            )
        },
        addDialog = { dismissDialog ->
            AddSleepDialog(
                viewModel = dialogViewModel,
                onSave = { start, end ->
                    viewModel.addSleep(start, end)
                    dismissDialog()
                },
                onDismiss = { dismissDialog() }
            )
        }
    )
}
