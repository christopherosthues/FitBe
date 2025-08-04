package org.darthacheron.fitbe.components.date.week

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import org.darthacheron.fitbe.components.date.YearWeek
import org.darthacheron.fitbe.utils.isoWeekAndYear

@Composable
fun WeekRangePicker(
    state: WeekRangePickerState,
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = {
        WeekRangePickerDefaults.WeekRangePickerTitle(
            modifier = Modifier.padding(WeekRangePickerTitlePadding)
        )
    },
    headline: (@Composable () -> Unit)? = {
        WeekRangePickerDefaults.WeekRangePickerHeadline(
            selectedStartYearWeek = state.selectedStartYearWeek,
            selectedEndYearWeek = state.selectedEndYearWeek,
            modifier = Modifier.padding(WeekRangePickerHeadlinePadding)
        )
    },
    colors: WeekRangePickerColors = WeekRangePickerDefaults.colors()
) {
    WeekEntryContainer(
        modifier = modifier,
        title = title,
        headline = headline,
        headlineTextStyle = MaterialTheme.typography.bodyMedium,
        headerMinHeight = 100.dp,
        colors = colors,
    ) {
        WeekRangePickerContent(
            selectedStartYearWeek = state.selectedStartYearWeek,
            selectedEndYearWeek = state.selectedEndYearWeek,
            onWeekRangeSelectionChange = { startYearWeek, endYearWeek ->
                state.setSelection(startYearWeek, endYearWeek)
            },
            yearRange = state.yearRange,
            colors = colors
        )
    }
}

@Composable
fun rememberWeekRangePickerState(
    initialSelectedStartYearWeek: YearWeek? = null,
    initialSelectedEndYearWeek: YearWeek? = null,
    yearRange: IntRange = WeekRangePickerDefaults.YearRange
): WeekRangePickerState {
    return remember {
        WeekRangePickerStateImpl(
            initialSelectedStartYearWeek = initialSelectedStartYearWeek,
            initialSelectedEndYearWeek = initialSelectedEndYearWeek,
            yearRange = yearRange
        )
    }
}

interface WeekRangePickerState {
    val selectedStartYearWeek: YearWeek?
    val selectedEndYearWeek: YearWeek?
    val yearRange: IntRange
    fun setSelection(startYearWeek: YearWeek?, endYearWeek: YearWeek?)
}

class WeekRangePickerStateImpl(
    initialSelectedStartYearWeek: YearWeek?,
    initialSelectedEndYearWeek: YearWeek?,
    override val yearRange: IntRange
) : WeekRangePickerState {
    override var selectedStartYearWeek: YearWeek? by mutableStateOf(initialSelectedStartYearWeek)
    override var selectedEndYearWeek: YearWeek? by mutableStateOf(initialSelectedEndYearWeek)

    override fun setSelection(startYearWeek: YearWeek?, endYearWeek: YearWeek?) {
        if (startYearWeek != null && endYearWeek != null) {
            require(startYearWeek <= endYearWeek) { "Start year week must be before or equal to end year week" }
            require(startYearWeek.weeksUntil(endYearWeek) <= 6) { "Range must be 6 weeks or less" }
        }
        selectedStartYearWeek = startYearWeek
        selectedEndYearWeek = endYearWeek
    }
}

@Composable
private fun WeekRangePickerContent(
    selectedStartYearWeek: YearWeek?,
    selectedEndYearWeek: YearWeek?,
    onWeekRangeSelectionChange: (startYearWeek: YearWeek?, endYearWeek: YearWeek?) -> Unit,
    yearRange: IntRange,
    colors: WeekRangePickerColors
) {
    val listState = rememberLazyListState()

    val isComplete = selectedStartYearWeek != null && selectedEndYearWeek != null
    val orderedStart = if (selectedStartYearWeek != null && selectedEndYearWeek != null)
        minOf(selectedStartYearWeek, selectedEndYearWeek) else null
    val orderedEnd = if (selectedStartYearWeek != null && selectedEndYearWeek != null)
        maxOf(selectedStartYearWeek, selectedEndYearWeek) else null
    val rangeLength = if (orderedStart != null && orderedEnd != null)
        orderedStart.weeksUntil(orderedEnd) else 0
    val isValid = isComplete && rangeLength <= 6

    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn(state = listState) {
            items(yearRange.toList()) { year ->
                val weeksInYear = getWeeksInYear(year)
                val chunkedWeeks = weeksInYear.chunked(4)

                this@LazyColumn.item {
                    Text(
                        text = "$year",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                this@LazyColumn.items(chunkedWeeks) { weekRow ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        weekRow.forEach { weekNum ->
                            val yw = YearWeek(year, weekNum)
                            val isInRange = orderedStart != null && orderedEnd != null && yw in orderedStart..orderedEnd
                            val isSelected = yw == selectedStartYearWeek || yw == selectedEndYearWeek

                            WeekButton(
                                yearWeek = yw,
                                isInRange = isInRange,
                                isSelected = isSelected,
                                onClick = {
                                    if (selectedStartYearWeek == null || (selectedStartYearWeek != null && selectedEndYearWeek != null)) {
                                        onWeekRangeSelectionChange(yw, null)
                                    } else {
                                        onWeekRangeSelectionChange(selectedStartYearWeek, yw)
                                    }
                                },
                                colors = colors
                            )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(16.dp))
                if (!isValid && isComplete) {
                    Text(
                        "Range must be 6 weeks or less.",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.semantics {
                            liveRegion = LiveRegionMode.Polite
                        }
                    )
                }

                Button(
                    onClick = {
                        if (orderedStart != null && orderedEnd != null) {
                            onWeekRangeSelectionChange(orderedStart, orderedEnd)
                        }
                    },
                    enabled = isValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirm Range")
                }
            }
        }
    }
}

fun getWeeksInYear(year: Int): List<Int> {
    var date = LocalDate(year, 12, 31)
    while (date.isoWeekAndYear().first != year) {
        date = date.minus(DatePeriod(days = 1))
    }
    val lastIsoWeek = date.isoWeekAndYear().second
    return (1..lastIsoWeek).toList()
}

@Composable
private fun WeekButton(
    yearWeek: YearWeek,
    isInRange: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    colors: WeekRangePickerColors
) {
    val backgroundColor = when {
        isSelected -> colors.selectedWeekContainerColor
        isInRange -> colors.weekInRangeContainerColor
        else -> colors.weekContainerColor
    }

    val contentColor = when {
        isSelected -> colors.selectedWeekContentColor
        isInRange -> colors.weekInRangeContentColor
        else -> colors.weekContentColor
    }

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(72.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .semantics {
                contentDescription = if (isSelected) {
                    "Selected week ${yearWeek.week} ${yearWeek.year}"
                } else if (isInRange) {
                    "Week ${yearWeek.week} ${yearWeek.year} in range"
                } else {
                    "Week ${yearWeek.week} ${yearWeek.year}"
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "W${yearWeek.week}",
            color = contentColor,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun WeekEntryContainer(
    modifier: Modifier,
    title: (@Composable () -> Unit)?,
    headline: (@Composable () -> Unit)?,
    headlineTextStyle: TextStyle,
    headerMinHeight: Dp,
    colors: WeekRangePickerColors,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .background(colors.containerColor)
    ) {
        if (title != null) {
            Box(modifier = Modifier.padding(WeekRangePickerTitlePadding)) {
                title()
            }
        }

        if (headline != null) {
            Box(
                modifier = Modifier
                    .padding(WeekRangePickerHeadlinePadding)
                    .fillMaxWidth()
            ) {
                headline()
            }
        }

        content()
    }
}

object WeekRangePickerDefaults {
    val YearRange: IntRange = IntRange(2000, 2999)

    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surface,
        titleContentColor: Color = MaterialTheme.colorScheme.onSurface,
        headlineContentColor: Color = MaterialTheme.colorScheme.onSurface,
        weekContentColor: Color = MaterialTheme.colorScheme.onSurface,
        weekInRangeContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
        selectedWeekContentColor: Color = MaterialTheme.colorScheme.onPrimary,
        weekContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        weekInRangeContainerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        selectedWeekContainerColor: Color = MaterialTheme.colorScheme.primary
    ): WeekRangePickerColors = WeekRangePickerColors(
        containerColor = containerColor,
        titleContentColor = titleContentColor,
        headlineContentColor = headlineContentColor,
        weekContentColor = weekContentColor,
        weekInRangeContentColor = weekInRangeContentColor,
        selectedWeekContentColor = selectedWeekContentColor,
        weekContainerColor = weekContainerColor,
        weekInRangeContainerColor = weekInRangeContainerColor,
        selectedWeekContainerColor = selectedWeekContainerColor
    )

    @Composable
    fun WeekRangePickerTitle(
        modifier: Modifier = Modifier
    ) {
        Text(
            text = "Select Week Range",
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier
        )
    }

    @Composable
    fun WeekRangePickerHeadline(
        selectedStartYearWeek: YearWeek?,
        selectedEndYearWeek: YearWeek?,
        modifier: Modifier = Modifier
    ) {
        val headlineText = when {
            selectedStartYearWeek == null && selectedEndYearWeek == null -> "No selection"
            selectedStartYearWeek != null && selectedEndYearWeek == null -> "Start: W${selectedStartYearWeek.week}/${selectedStartYearWeek.year}"
            else -> "Range: W${selectedStartYearWeek?.week}/${selectedStartYearWeek?.year} - W${selectedEndYearWeek?.week}/${selectedEndYearWeek?.year}"
        }

        Text(
            text = headlineText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.semantics {
                liveRegion = LiveRegionMode.Polite
            }
        )
    }
}

class WeekRangePickerColors(
    val containerColor: Color,
    val titleContentColor: Color,
    val headlineContentColor: Color,
    val weekContentColor: Color,
    val weekInRangeContentColor: Color,
    val selectedWeekContentColor: Color,
    val weekContainerColor: Color,
    val weekInRangeContainerColor: Color,
    val selectedWeekContainerColor: Color
)

private val WeekRangePickerTitlePadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp)
private val WeekRangePickerHeadlinePadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
