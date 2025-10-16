package org.darthacheron.fitbe.health.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.local_time_format
import fitbe.composeapp.generated.resources.steps_daily_view_steps
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.components.DailyView
import org.darthacheron.fitbe.health.components.format
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun StepsDailyView(
    stepsDailyViewModel: StepsDailyViewModel,
    addStepsDialogViewModel: AddStepsDialogViewModel
) {
    DailyView(
        dailyViewModel = stepsDailyViewModel,
        detailView = { state, date ->
            val maxSteps by stepsDailyViewModel.maxSteps.collectAsState()
            val targetSteps by stepsDailyViewModel.targetSteps.collectAsState()
            StepsDetailView(
                state = state,
                maxSteps = maxSteps,
                targetSteps = targetSteps
            )
        },
        addDialog = { onDismiss ->
            AddStepsDialog(
                viewModel = addStepsDialogViewModel,
                onSave = { date, steps ->
                    stepsDailyViewModel.addSteps(date, steps)
                    onDismiss()
                },
                onDismiss = { onDismiss() }
            )
        }
    )
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun StepsDetailView(
    state: StepsDailyUiState,
    maxSteps: UInt,
    targetSteps: Int
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
        ) {
            item {
                PlotDailySteps(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    stepsData = state.steps,
                    times = state.times,
                    maxSteps = maxSteps,
                    targetSteps = targetSteps
                )
            }
            items(
                items = state.steps,
                key = { it.id }
            ) { steps ->
                StepsListItem(
                    steps = steps
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
private fun StepsListItem(steps: Steps) {
    ListItem(
        headlineContent = {
            Text(
                text = stringResource(
                    Res.string.steps_daily_view_steps,
                    steps.steps
                ),
                fontWeight = FontWeight.Bold
            )
        },
        overlineContent = {
            Text(
                text = steps.date.toLocalDateTime(TimeZone.currentSystemDefault()).time.format(
                    stringResource(Res.string.local_time_format)
                )
            )
        },
        supportingContent = { null }
    )
}