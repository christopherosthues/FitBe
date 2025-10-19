package org.darthacheron.fitbe.health.steps

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.health.components.OverviewView
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StepsOverviewView(
    stepsOverviewViewModel: StepsOverviewViewModel = koinViewModel()
) {
    OverviewView(
        overviewViewModel = stepsOverviewViewModel,
        plot = { state, dateRange ->
            PlotSteps(
                modifier = Modifier.padding(top = 8.dp, bottom = 64.dp),
                stepsData = state.steps,
                dateRange = dateRange,
                dates = state.dates,
                maxSteps = state.maxSteps,
                thumbnail = false,
                targetSteps = state.target
            )
        },
        addDialog = { dismissDialog ->
            AddStepsDialog(
                onSave = { date, steps ->
                    stepsOverviewViewModel.addSteps(date, steps)
                    dismissDialog()
                },
                onDismiss = { dismissDialog() }
            )
        }
    )
}