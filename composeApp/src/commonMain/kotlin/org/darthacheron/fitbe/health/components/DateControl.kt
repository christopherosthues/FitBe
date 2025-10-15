package org.darthacheron.fitbe.health.components

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
import fitbe.composeapp.generated.resources.local_date_format
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DatePickerModal
import org.darthacheron.fitbe.ui.UiState
import org.darthacheron.fitbe.ui.UiStateError
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun <Error : UiStateError, State : UiState<Error>> DateControl(
    date: Instant,
    dailyViewModel: DailyViewModel<Error, State>
) {
    var showDateRangeDialog by remember { mutableStateOf(false) }

    TextButton(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = { showDateRangeDialog = true }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = date.toLocalDateTime(TimeZone.currentSystemDefault()).date.format(stringResource(Res.string.local_date_format))
            )
            Icon(
                painterResource(Res.drawable.ic_date_range),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 8.dp)
            )
        }
    }

    if (showDateRangeDialog) {
        DatePickerModal(
            onDateSelected = { newDate ->
                if (newDate != null) {
                    dailyViewModel.setDate(
                        Instant.fromEpochMilliseconds(newDate)
                    )
                }
                showDateRangeDialog = false
            },
            onDismiss = { showDateRangeDialog = false }
        )
    }
}