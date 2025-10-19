package org.darthacheron.fitbe.health.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_delete
import fitbe.composeapp.generated.resources.ic_edit
import fitbe.composeapp.generated.resources.local_time_format
import fitbe.composeapp.generated.resources.steps_daily_view_steps
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.components.DailyView
import org.darthacheron.fitbe.health.components.format
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun StepsDailyView(
    stepsDailyViewModel: StepsDailyViewModel = koinViewModel()
) {
    DailyView(
        dailyViewModel = stepsDailyViewModel,
        detailView = { state, date ->
            StepsDetailView(
                state = state,
                maxSteps = state.maxSteps,
                targetSteps = state.target,
                stepsDailyViewModel = stepsDailyViewModel
            )
        },
        addDialog = { date, onDismiss ->
            AddStepsDialog(
                initialDate = date,
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
    maxSteps: Int,
    targetSteps: Int?,
    stepsDailyViewModel: StepsDailyViewModel
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedStepsId by remember { mutableStateOf<Uuid?>(null) }

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
                    steps = steps,
                    editDialog = { id ->
                        showEditDialog = true
                        selectedStepsId = id
                    },
                    delete = stepsDailyViewModel::deleteSteps
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }

    if (showEditDialog && selectedStepsId != null) {
        EditStepsDialog(
            id = selectedStepsId!!,
            onSave = { id, date, steps ->
                stepsDailyViewModel.editSteps(id, date, steps)
                showEditDialog = false
                selectedStepsId = null
            },
            onDismiss = {
                showEditDialog = false
                selectedStepsId = null
            }
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun StepsListItem(
    steps: Steps,
    editDialog: (id: Uuid) -> Unit,
    delete: (id: Uuid) -> Unit
) {
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
        trailingContent = {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { editDialog(steps.id) }
                ) {
                    Icon(painter = painterResource(Res.drawable.ic_edit), contentDescription = null)
                }
                IconButton(
                    onClick = { delete(steps.id) }
                ) {
                    Icon(painter = painterResource(Res.drawable.ic_delete), contentDescription = null)
                }
            }
        },
        supportingContent = { null }
    )
}