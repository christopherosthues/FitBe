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
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.body_weight_daily_view_body_fat
import fitbe.composeapp.generated.resources.body_weight_daily_view_body_water
import fitbe.composeapp.generated.resources.body_weight_daily_view_body_weight
import fitbe.composeapp.generated.resources.body_weight_daily_view_bone_mass
import fitbe.composeapp.generated.resources.body_weight_daily_view_muscle_mass
import fitbe.composeapp.generated.resources.ic_sleep
import fitbe.composeapp.generated.resources.local_time_format
import fitbe.composeapp.generated.resources.sleep_daily_view_sleep_time
import fitbe.composeapp.generated.resources.sleep_daily_view_total_sleep_last_night
import fitbe.composeapp.generated.resources.sleep_daily_view_total_sleep_time
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.components.DailyView
import org.darthacheron.fitbe.health.components.format
import org.darthacheron.fitbe.health.weight.PlotDailyBodyWeights
import org.darthacheron.fitbe.settings.WeightUnit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun SleepDailyView(
    sleepDailyViewModel: SleepDailyViewModel,
    addSleepDialogViewModel: AddSleepDialogViewModel
) {
    DailyView(
        dailyViewModel = sleepDailyViewModel,
        detailView = { state, date ->
            SleepDetailView(
                state = state,
                date = date.toLocalDateTime(TimeZone.currentSystemDefault()).date,
                targetSleepDuration = state.target,
                maxSleeps = state.maxSleeps
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
    date: LocalDate,
    targetSleepDuration: Int?,
    maxSleeps: Int
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
                    sleep = sleep
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
private fun SleepListItem(sleep: Sleep) {
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
            Text(
                text =
                    stringResource(
                        Res.string.sleep_daily_view_sleep_time,
                        sleep.start.toLocalDateTime(TimeZone.currentSystemDefault()).time.format(
                            stringResource(Res.string.local_time_format)),
                        sleep.end.toLocalDateTime(TimeZone.currentSystemDefault()).time.format(
                            stringResource(Res.string.local_time_format))
                    )
                )
        },
        supportingContent = { null }
    )
}