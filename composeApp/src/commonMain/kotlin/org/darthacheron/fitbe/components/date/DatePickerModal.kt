package org.darthacheron.fitbe.components.date

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.date_range_picker_cancel
import fitbe.composeapp.generated.resources.date_range_picker_ok
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.PastOrPresentSelectableDates
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    initialSelectedDateMillis: Long? =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.UTC)
            .toInstant(TimeZone.UTC)
            .toEpochMilliseconds()
) {
    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = initialSelectedDateMillis,
            selectableDates = PastOrPresentSelectableDates
        )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(text = stringResource(Res.string.date_range_picker_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.date_range_picker_cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}