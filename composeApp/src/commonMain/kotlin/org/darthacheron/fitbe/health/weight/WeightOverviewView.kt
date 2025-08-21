package org.darthacheron.fitbe.health.weight


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_arrow_back
import fitbe.composeapp.generated.resources.ic_arrow_forward
import fitbe.composeapp.generated.resources.ic_date_range
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DateRangePickerModal
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
fun WeightOverviewView(
    bodyWeightOverviewViewModel: WeightOverviewViewModel,
    settingsRepository: SettingsRepository
) {
    val bodyWeights by bodyWeightOverviewViewModel.bodyWeights.collectAsState()
    val maxBodyWeight by bodyWeightOverviewViewModel.maxWeight.collectAsState()
    val settings by settingsRepository.getSettingsFlow().collectAsState(Settings())
    val dateRange by bodyWeightOverviewViewModel.dateRange.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!bodyWeights.isEmpty()) {
            PlotBodyWeights(
                bodyWeightOverviewViewModel,
                bodyWeights,
                settings,
                maxBodyWeight,
                false
            )
        }
        IconButton(
            onClick = { bodyWeightOverviewViewModel.movePast() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = null
            )
        }
        IconButton(
            onClick = { bodyWeightOverviewViewModel.moveFuture() },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_forward),
                contentDescription = null
            )
        }
        Row(
            modifier = Modifier.align(Alignment.BottomEnd).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            DateRangeControl(
                dateRange,
                bodyWeightOverviewViewModel
            )

            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF2196F3),
                modifier = Modifier
                    .padding(16.dp),
            ) {
                Icon(painter = painterResource(Res.drawable.ic_add), contentDescription = null)
            }
        }
    }

    if (showAddDialog) {
        AddBodyWeightDialog(
            settings = settings,
            onSave = { date,
                       weightInKg,
                       bodyFatPercentage,
                       muscleMassInKg,
                       boneMassInKg,
                       bodyWaterInPercentage ->
                bodyWeightOverviewViewModel.addBodyWeight(
                    date,
                    weightInKg,
                    bodyFatPercentage,
                    muscleMassInKg,
                    boneMassInKg,
                    bodyWaterInPercentage
                )
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
private fun DateRangeControl(
    dateRange: DateRange,
    bodyWeightOverviewViewModel: WeightOverviewViewModel,
) {

    var showDateRangeDialog by remember { mutableStateOf(false) }
    TextButton(
        modifier = Modifier.padding(16.dp),
        onClick = { showDateRangeDialog = true },
    ) {
        Row {
            Column {
                Text(
                    text =
                        dateRange.startDate.toLocalDateTime(TimeZone.UTC).date.toString()
                )
                Text(
                    text =
                        dateRange.endDate.toLocalDateTime(TimeZone.UTC).date.toString()
                )
            }
            Icon(
                painterResource(Res.drawable.ic_date_range),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 8.dp)
            )
        }
    }

    if (showDateRangeDialog) {
        DateRangePickerModal(
            onDateRangeSelected = { dateRange, selectedDateUnit ->
                if (dateRange.first != null && dateRange.second != null) {
                    bodyWeightOverviewViewModel.setRange(
                        Instant.fromEpochMilliseconds(dateRange.first!!),
                        Instant.fromEpochMilliseconds(dateRange.second!!),
                        selectedDateUnit
                    )
                }
                showDateRangeDialog = false
            },
            onDismiss = { showDateRangeDialog = false }
        )
    }
}
