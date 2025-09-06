package org.darthacheron.fitbe.health.beverages


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_arrow_back
import fitbe.composeapp.generated.resources.ic_arrow_forward
import fitbe.composeapp.generated.resources.ic_date_range
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.DateRangePickerModal
import org.darthacheron.fitbe.health.steps.PlotSteps
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BeverageOverviewView(
    beverageOverviewViewModel: BeverageOverviewViewModel,
) {
    LaunchedEffect(Unit) {
        beverageOverviewViewModel.updateTopBarConfig()
    }
    val beverages by beverageOverviewViewModel.beverages.collectAsState()
    val dateRange by beverageOverviewViewModel.dateRangeFlow.collectAsState()
    val targetBeverages by beverageOverviewViewModel.targetBeverages.collectAsState()
    val maxBeverages by beverageOverviewViewModel.maxBeverages.collectAsState()

    val dates = beverageOverviewViewModel.dates(beverages)

    Box(modifier = Modifier.fillMaxSize()) {
        PlotBeverages(
            Modifier.padding(bottom = 64.dp),
            beverages,
            dateRange,
            dates,
            maxBeverages,
            false,
            targetBeverages,
        )

        IconButton(
            onClick = { beverageOverviewViewModel.movePast() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = null
            )
        }

        IconButton(
            onClick = { beverageOverviewViewModel.moveFuture() },
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
                beverageOverviewViewModel
            )
        }
    }
}

@Composable
private fun DateRangeControl(
    dateRange: DateRange,
    beverageOverviewViewModel: BeverageOverviewViewModel,
) {
    var showDateRangeDialog by remember { mutableStateOf(false) }

    TextButton(
        modifier = Modifier.padding(16.dp),
        onClick = { showDateRangeDialog = true },
    ) {
        Row {
            Column {
                Text(
                    text = dateRange.startDate.toLocalDateTime(TimeZone.UTC).date.toString()
                )
                Text(
                    text = dateRange.endDate.toLocalDateTime(TimeZone.UTC).date.toString()
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
                    beverageOverviewViewModel.setRange(
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
