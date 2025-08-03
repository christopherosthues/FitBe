package org.darthacheron.fitbe.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.WeekRangePicker
import org.darthacheron.fitbe.components.date.year.Year
import org.darthacheron.fitbe.components.date.year.YearRangePicker
import org.darthacheron.fitbe.components.date.YearWeek
import org.darthacheron.fitbe.components.date.month.MonthRangePicker
import org.darthacheron.fitbe.components.date.month.YearMonth
import org.darthacheron.fitbe.components.date.month.rememberMonthRangePickerState
import org.darthacheron.fitbe.components.date.rememberWeekRangePickerState
import org.darthacheron.fitbe.components.date.year.rememberYearRangePickerState
import org.darthacheron.fitbe.utils.isoWeekAndYear
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
    var selectedDateUnitIndex by remember { mutableStateOf(dateUnits.indexOf(dateUnit)) }
    var selectedDateUnit by remember { mutableStateOf(dateUnits[selectedDateUnitIndex]) }
    val now = Clock.System.now()
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = now.toEpochMilliseconds(),
        initialSelectedEndDateMillis = now.toEpochMilliseconds(),
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = PastOrPresentSelectableDates
    )
    val initialWeek = now.toLocalDateTime(TimeZone.UTC).date.isoWeekAndYear()
    val weekRangePickerState = rememberWeekRangePickerState(
        initialSelectedStartYearWeek = YearWeek(initialWeek.first, initialWeek.second),
        initialSelectedEndYearWeek = YearWeek(initialWeek.first, initialWeek.second),
    )
    val initialMonth = now.toLocalDateTime(TimeZone.UTC).date.monthNumber
    val initialYear = now.toLocalDateTime(TimeZone.UTC).date.year
    val monthRangePickerState = rememberMonthRangePickerState(
        initialSelectedStartYearMonth = YearMonth(initialYear, initialMonth),
        initialSelectedEndYearMonth = YearMonth(initialYear, initialMonth),
    )
    val yearRangePickerState = rememberYearRangePickerState(
        initialSelectedStartYear = Year(initialYear),
        initialSelectedEndYear = Year(initialYear),
    )

    DatePickerDialog(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    // TODO
                    var startDateMillis: Long?
                    var endDateMillis: Long?
                    when (selectedDateUnit) {
                        DateUnit.DAY -> {
                            startDateMillis = dateRangePickerState.selectedStartDateMillis
                            endDateMillis = dateRangePickerState.selectedEndDateMillis
                        }
                        DateUnit.WEEK -> {
                            startDateMillis = dateRangePickerState.selectedStartDateMillis
                            endDateMillis = dateRangePickerState.selectedEndDateMillis
                        }
                        DateUnit.MONTH -> {
                            startDateMillis = dateRangePickerState.selectedStartDateMillis
                            endDateMillis = dateRangePickerState.selectedEndDateMillis
                        }
                        DateUnit.YEAR -> {
                            startDateMillis = dateRangePickerState.selectedStartDateMillis
                            endDateMillis = dateRangePickerState.selectedEndDateMillis
                        }
                    }
                    onDateRangeSelected(
                        Pair(startDateMillis, endDateMillis),
                        selectedDateUnit
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
        Column(modifier = Modifier.padding(top = 16.dp)) {
            DropdownSelection(
                initialState = false,
                selectedIndex = selectedDateUnitIndex,
                items = dateUnits,
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
                    selectedDateUnitIndex = it
                    selectedDateUnit = dateUnits[selectedDateUnitIndex]
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            when (selectedDateUnit) {
                DateUnit.DAY -> {
                    DateRangePicker(
                        state = dateRangePickerState,
                        title = null,
                        showModeToggle = false,
                        headline = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .padding(16.dp)
                    )
                }

                DateUnit.WEEK -> {
                    WeekRangePicker(
                        state = weekRangePickerState
//                        onRangeSelected = { start, end ->
//                            onDateRangeSelected(
//                                Pair(start.toEpochMilli(), end.toEpochMilli()),
//                                selectedDateUnit
//                            )
//                            onDismiss()
//                        }
                    )
                }

                DateUnit.MONTH -> {
                    MonthRangePicker(
                        state = monthRangePickerState,
                        headline = null,
                        title = null,
//                        onRangeSelected = { start, end ->
//                            onDateRangeSelected(
//                                Pair(start.toEpochMilli(), end.toEpochMilli()),
//                                selectedDateUnit
//                            )
//                            onDismiss()
//                        }
                    )
                }

                DateUnit.YEAR -> {
                    YearRangePicker(
                        state = yearRangePickerState,
                        headline = null,
                        title = null,
//                        onRangeSelected = { start, end ->
//                            onDateRangeSelected(
//                                Pair(start.toEpochMilli(), end.toEpochMilli()),
//                                selectedDateUnit
//                            )
//                            onDismiss()
//                        }
                    )
                }
            }
        }
    }
}