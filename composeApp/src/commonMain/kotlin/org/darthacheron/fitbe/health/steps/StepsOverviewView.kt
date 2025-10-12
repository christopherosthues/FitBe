package org.darthacheron.fitbe.health.steps

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.health.components.OverviewView

@Composable
fun StepsOverviewView(
    stepsOverviewViewModel: StepsOverviewViewModel,
    addStepsDialogViewModel: AddStepsDialogViewModel
) {
    OverviewView(
        overviewViewModel = stepsOverviewViewModel,
        plot = { state, dateRange ->
            val targetSteps by stepsOverviewViewModel.targetSteps.collectAsState()
            val maxSteps by stepsOverviewViewModel.maxSteps.collectAsState()
            PlotSteps(
                Modifier.padding(top = 8.dp, bottom = 64.dp),
                state.steps,
                dateRange,
                state.dates,
                maxSteps,
                false,
                targetSteps
            )
        },
        addDialog = { dismissDialog ->
            AddStepsDialog(
                viewModel = addStepsDialogViewModel,
                onSave = { date, steps ->
                    stepsOverviewViewModel.addSteps(date, steps)
                    dismissDialog()
                },
                onDismiss = { dismissDialog() }
            )
        }
    )
}