package org.darthacheron.fitbe.components.date

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeInputDialog(
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState =
        rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = true
        )
    AdvancedTimePickerDialog(
        onDismiss = onDismiss,
        onConfirm = { onTimeSelected(timePickerState.hour, timePickerState.minute) }
    ) {
        TimeInput(state = timePickerState)
    }
}