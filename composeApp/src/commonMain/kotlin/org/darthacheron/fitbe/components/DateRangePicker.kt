package org.darthacheron.fitbe.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.chart_grouping
import org.darthacheron.fitbe.utils.PastOrPresentSelectableDates
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun DateRangePickerModal(
    dateUnit: DateUnit = DateUnit.DAY,
    onDateRangeSelected: (Pair<Long?, Long?>, DateUnit) -> Unit,
    onDismiss: () -> Unit
) {
    val dateUnits = DateUnit.entries
    var selectedViewTypeIndex by remember { mutableStateOf(dateUnits.indexOf(dateUnit)) }
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = Clock.System.now().toEpochMilliseconds(),
        initialSelectedEndDateMillis = Clock.System.now().toEpochMilliseconds(),
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = PastOrPresentSelectableDates,
        yearRange = DatePickerDefaults.YearRange
    )

    DatePickerDialog(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        ),
                        dateUnits[selectedViewTypeIndex]
                    )
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        Column {
            DropdownSelection(
                initialState = false,
                selectedIndex = selectedViewTypeIndex,
                items = DateUnit.entries,
                title = stringResource(Res.string.chart_grouping),
                itemContent = { item, onClick ->
                    DropdownMenuItem(
                        text = { Text(item.localizedString()) },
                        onClick = onClick
                    )
                },
                itemToString = {
                    it.localizedString()
                },
                onItemSelected = {
                    selectedViewTypeIndex = it
                }
            )
        }
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = "Select date range"
                )
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }
}