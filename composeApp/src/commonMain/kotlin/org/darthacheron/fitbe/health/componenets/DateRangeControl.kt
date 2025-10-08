package org.darthacheron.fitbe.health.componenets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_date_range
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.DateRangePickerModal
import org.darthacheron.fitbe.ui.UiState
import org.darthacheron.fitbe.ui.UiStateError
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun <Error : UiStateError, State : UiState<Error>> DateRangeControl(
    dateRange: DateRange,
    overviewViewModel: OverviewViewModel<Error, State>,
) {
    var showDateRangeDialog by remember { mutableStateOf(false) }

    TextButton(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = { showDateRangeDialog = true },
    ) {
        Row {
            Column {
                Text(
                    text = dateRange.startDate.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
                )
                Text(
                    text = dateRange.endDate.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
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
            onDateRangeSelected = { newDateRange, selectedDateUnit ->
                if (newDateRange.first != null && newDateRange.second != null) {
                    overviewViewModel.setRange(
                        Instant.fromEpochMilliseconds(newDateRange.first!!),
                        Instant.fromEpochMilliseconds(newDateRange.second!!),
                        selectedDateUnit
                    )
                }
                showDateRangeDialog = false
            },
            onDismiss = { showDateRangeDialog = false }
        )
    }
}
