package org.darthacheron.fitbe.components.date

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.date_range_picker_cancel
import fitbe.composeapp.generated.resources.date_range_picker_ok
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.month.MonthRangePicker
import org.darthacheron.fitbe.components.date.month.YearMonth
import org.darthacheron.fitbe.components.date.month.rememberMonthRangePickerState
import org.darthacheron.fitbe.components.date.week.WeekRangePicker
import org.darthacheron.fitbe.components.date.week.YearWeek
import org.darthacheron.fitbe.components.date.week.rememberWeekRangePickerState
import org.darthacheron.fitbe.components.date.year.Year
import org.darthacheron.fitbe.components.date.year.YearRangePicker
import org.darthacheron.fitbe.components.date.year.rememberYearRangePickerState
import org.darthacheron.fitbe.components.date.PastOrPresentSelectableDates
import org.darthacheron.fitbe.components.date.month.PastOrPresentSelectableMonths
import org.darthacheron.fitbe.components.date.week.PastOrPresentSelectableWeeks
import org.darthacheron.fitbe.components.date.year.PastOrPresentSelectableYears
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
    var selectedDateUnit by remember { mutableStateOf(dateUnit) }
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
        selectableWeeks = PastOrPresentSelectableWeeks
    )
    val initialMonth = now.toLocalDateTime(TimeZone.UTC).date.monthNumber
    val initialYear = now.toLocalDateTime(TimeZone.UTC).date.year
    val monthRangePickerState = rememberMonthRangePickerState(
        initialSelectedStartYearMonth = YearMonth(initialYear, initialMonth),
        initialSelectedEndYearMonth = YearMonth(initialYear, initialMonth),
        selectableMonths = PastOrPresentSelectableMonths
    )
    val yearRangePickerState = rememberYearRangePickerState(
        initialSelectedStartYear = Year(initialYear),
        initialSelectedEndYear = Year(initialYear),
        selectableYears = PastOrPresentSelectableYears
    )

    DatePickerDialog(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    var startDateMillis: Long?
                    var endDateMillis: Long?
                    when (selectedDateUnit) {
                        DateUnit.DAY -> {
                            startDateMillis = dateRangePickerState.selectedStartDateMillis
                            endDateMillis = dateRangePickerState.selectedEndDateMillis
                        }
                        DateUnit.WEEK -> {
                            startDateMillis = weekRangePickerState.selectedStartWeek?.startDateMillis()
                            endDateMillis = weekRangePickerState.selectedEndWeek?.endDateMillis()
                        }
                        DateUnit.MONTH -> {
                            startDateMillis = monthRangePickerState.selectedStartMonth?.startDateMillis()
                            endDateMillis = monthRangePickerState.selectedEndMonth?.endDateMillis()
                        }
                        DateUnit.YEAR -> {
                            startDateMillis = yearRangePickerState.selectedStartYear?.startDateMillis()
                            endDateMillis = yearRangePickerState.selectedEndYear?.endDateMillis()
                        }
                    }
                    onDateRangeSelected(
                        Pair(startDateMillis, endDateMillis),
                        selectedDateUnit
                    )
                    onDismiss()
                }
            ) {
                Text(text = stringResource(Res.string.date_range_picker_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.date_range_picker_cancel))
            }
        }
    ) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                dateUnits.forEach {
                    FilterChip(
                        selected = selectedDateUnit == it,
                        onClick = {
                            selectedDateUnit = it
                        },
                        label = {
                            Text(it.localizedString())
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

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
                        state = weekRangePickerState,
                        headline = null,
                        title = null,
                    )
                }

                DateUnit.MONTH -> {
                    MonthRangePicker(
                        state = monthRangePickerState,
                        headline = null,
                        title = null,
                    )
                }

                DateUnit.YEAR -> {
                    YearRangePicker(
                        state = yearRangePickerState,
                        headline = null,
                        title = null,
                    )
                }
            }
        }
    }
}