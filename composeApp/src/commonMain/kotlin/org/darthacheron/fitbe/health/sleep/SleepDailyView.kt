package org.darthacheron.fitbe.health.sleep

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
import androidx.compose.material3.MaterialTheme
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
import fitbe.composeapp.generated.resources.beverages_daily_view_content_description_delete
import fitbe.composeapp.generated.resources.beverages_daily_view_content_description_edit
import fitbe.composeapp.generated.resources.ic_delete
import fitbe.composeapp.generated.resources.ic_edit
import fitbe.composeapp.generated.resources.ic_sleep
import fitbe.composeapp.generated.resources.local_time_format
import fitbe.composeapp.generated.resources.sleep_daily_view_content_description_delete
import fitbe.composeapp.generated.resources.sleep_daily_view_content_description_edit
import fitbe.composeapp.generated.resources.sleep_daily_view_sleep_time
import fitbe.composeapp.generated.resources.sleep_daily_view_total_sleep_last_night
import fitbe.composeapp.generated.resources.sleep_daily_view_total_sleep_time
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.components.DailyView
import org.darthacheron.fitbe.health.components.format
import org.darthacheron.fitbe.health.sleep.manage.AddSleepDialog
import org.darthacheron.fitbe.health.sleep.manage.EditSleepDialog
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun SleepDailyView(
    sleepDailyViewModel: SleepDailyViewModel = koinViewModel()
) {
    DailyView(
        dailyViewModel = sleepDailyViewModel,
        detailView = { state, date ->
            SleepDetailView(
                state = state,
                date = date.toLocalDateTime(TimeZone.currentSystemDefault()).date,
                targetSleepDuration = state.target,
                maxSleeps = state.maxSleeps,
                sleepDailyViewModel = sleepDailyViewModel
            )
        },
        addDialog = { date, onDismiss ->
            AddSleepDialog(
                date = date,
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
    date: LocalDate,
    targetSleepDuration: Int?,
    maxSleeps: Int,
    sleepDailyViewModel: SleepDailyViewModel
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedSleepId by remember { mutableStateOf<Uuid?>(null) }

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
                PlotDailySleep(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    date = date,
                    totalSleep = state.totalSleep,
                    maxSleeps = maxSleeps,
                    targetSleepDuration = targetSleepDuration
                )
            }
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text =
                                stringResource(
                                    Res.string.sleep_daily_view_total_sleep_last_night,
                                    state.sleepHours,
                                    state.sleepMinutes
                                ),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    overlineContent = { null },
                    supportingContent = { null }
                )
            }
            items(
                items = state.sleeps,
                key = { it.id }
            ) { sleep ->
                SleepListItem(
                    sleep = sleep,
                    editDialog = { id ->
                        showEditDialog = true
                        selectedSleepId = id
                    },
                    delete = sleepDailyViewModel::deleteSleep
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }

    if (showEditDialog && selectedSleepId != null) {
        EditSleepDialog(
            id = selectedSleepId!!,
            onSave = { id, start, end ->
                sleepDailyViewModel.editSleep(id, start, end)
                showEditDialog = false
                selectedSleepId = null
            },
            onDismiss = {
                showEditDialog = false
                selectedSleepId = null
            }
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun SleepListItem(
    sleep: Sleep,
    editDialog: (id: Uuid) -> Unit,
    delete: (id: Uuid) -> Unit
) {
    val timeText = stringResource(
        Res.string.sleep_daily_view_sleep_time,
        sleep.start.toLocalDateTime(TimeZone.currentSystemDefault()).time.format(
            stringResource(Res.string.local_time_format)
        ),
        sleep.end.toLocalDateTime(TimeZone.currentSystemDefault()).time.format(
            stringResource(Res.string.local_time_format)
        )
    )
    ListItem(
        leadingContent = {
            Icon(painter = painterResource(Res.drawable.ic_sleep), contentDescription = null)
        },
        headlineContent = {
            Text(
                text = stringResource(Res.string.sleep_daily_view_total_sleep_time, sleep.hours, sleep.minutes),
                fontWeight = FontWeight.Bold
            )
        },
        overlineContent = {
            Text(text = timeText)
        },
        trailingContent = {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { editDialog(sleep.id) }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_edit),
                        contentDescription =
                            stringResource(Res.string.sleep_daily_view_content_description_edit, timeText)
                    )
                }
                IconButton(
                    onClick = { delete(sleep.id) }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_delete),
                        contentDescription =
                            stringResource(Res.string.sleep_daily_view_content_description_delete, timeText)
                    )
                }
            }
        },
        supportingContent = { null }
    )
}