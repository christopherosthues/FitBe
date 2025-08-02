package org.darthacheron.fitbe.components.date

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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.components.date.MonthRangePickerDefaults.MonthNames

@Composable
fun MonthRangePicker(
    state: MonthRangePickerState,
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = {
        MonthRangePickerDefaults.MonthRangePickerTitle(
            modifier = Modifier.padding(MonthRangePickerTitlePadding)
        )
    },
    headline: (@Composable () -> Unit)? = {
        MonthRangePickerDefaults.MonthRangePickerHeadline(
            selectedStartYearMonth = state.selectedStartYearMonth,
            selectedEndYearMonth = state.selectedEndYearMonth,
            modifier = Modifier.padding(MonthRangePickerHeadlinePadding)
        )
    },
    colors: MonthRangePickerColors = MonthRangePickerDefaults.colors()
) {
    MonthEntryContainer(
        modifier = modifier,
        title = title,
        headline = headline,
        headlineTextStyle = MaterialTheme.typography.bodyMedium,
        headerMinHeight = 100.dp,
        colors = colors,
    ) {
        MonthRangePickerContent(
            selectedStartYearMonth = state.selectedStartYearMonth,
            selectedEndYearMonth = state.selectedEndYearMonth,
            onMonthRangeSelectionChange = { startYearMonth, endYearMonth ->
                state.setSelection(startYearMonth, endYearMonth)
            },
            yearRange = state.yearRange,
            colors = colors
        )
    }
}

@Composable
fun rememberMonthRangePickerState(
    initialSelectedStartYearMonth: YearMonth? = null,
    initialSelectedEndYearMonth: YearMonth? = null,
    yearRange: IntRange = MonthRangePickerDefaults.YearRange
): MonthRangePickerState {
    return remember {
        MonthRangePickerStateImpl(
            initialSelectedStartYearMonth = initialSelectedStartYearMonth,
            initialSelectedEndYearMonth = initialSelectedEndYearMonth,
            yearRange = yearRange
        )
    }
}

interface MonthRangePickerState {
    val selectedStartYearMonth: YearMonth?
    val selectedEndYearMonth: YearMonth?
    val yearRange: IntRange
    fun setSelection(startYearMonth: YearMonth?, endYearMonth: YearMonth?)
}

class MonthRangePickerStateImpl(
    initialSelectedStartYearMonth: YearMonth?,
    initialSelectedEndYearMonth: YearMonth?,
    override val yearRange: IntRange
) : MonthRangePickerState {
    override var selectedStartYearMonth: YearMonth? by mutableStateOf(initialSelectedStartYearMonth)
    override var selectedEndYearMonth: YearMonth? by mutableStateOf(initialSelectedEndYearMonth)

    override fun setSelection(startYearMonth: YearMonth?, endYearMonth: YearMonth?) {
        if (startYearMonth != null && endYearMonth != null) {
            require(startYearMonth <= endYearMonth) { "Start year month must be before or equal to end year month" }
            require(startYearMonth.monthsUntil(endYearMonth) <= 6) { "Range must be 6 months or less" }
        }
        selectedStartYearMonth = startYearMonth
        selectedEndYearMonth = endYearMonth
    }
}

@Composable
private fun MonthRangePickerContent(
    selectedStartYearMonth: YearMonth?,
    selectedEndYearMonth: YearMonth?,
    onMonthRangeSelectionChange: (startYearMonth: YearMonth?, endYearMonth: YearMonth?) -> Unit,
    yearRange: IntRange,
    colors: MonthRangePickerColors
) {
    val listState = rememberLazyListState()

    val isComplete = selectedStartYearMonth != null && selectedEndYearMonth != null
    val orderedStart = if (selectedStartYearMonth != null && selectedEndYearMonth != null)
        minOf(selectedStartYearMonth, selectedEndYearMonth) else null
    val orderedEnd = if (selectedStartYearMonth != null && selectedEndYearMonth != null)
        maxOf(selectedStartYearMonth, selectedEndYearMonth) else null
    val rangeLength = if (orderedStart != null && orderedEnd != null)
        orderedStart.monthsUntil(orderedEnd) else 0
    val isValid = isComplete && rangeLength <= 6

    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn(state = listState) {
            items(yearRange.toList()) { year ->
                this@LazyColumn.item {
                    Text(
                        text = "$year",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                this@LazyColumn.item {
                    val months = (1..12).map { month -> YearMonth(year, month) }
                    val chunkedMonths = months.chunked(4)

                    chunkedMonths.forEach { rowMonths ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            rowMonths.forEach { yearMonth ->
                                val isInRange = orderedStart != null && orderedEnd != null && yearMonth in orderedStart..orderedEnd
                                val isSelected = yearMonth == selectedStartYearMonth || yearMonth == selectedEndYearMonth

                                MonthButton(
                                    yearMonth = yearMonth,
                                    isInRange = isInRange,
                                    isSelected = isSelected,
                                    onClick = {
                                        if (selectedStartYearMonth == null || (selectedStartYearMonth != null && selectedEndYearMonth != null)) {
                                            onMonthRangeSelectionChange(yearMonth, null)
                                        } else {
                                            onMonthRangeSelectionChange(selectedStartYearMonth, yearMonth)
                                        }
                                    },
                                    colors = colors
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(16.dp))
                if (!isValid && isComplete) {
                    Text(
                        "Range must be 6 months or less.",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.semantics {
                            liveRegion = androidx.compose.ui.semantics.LiveRegionMode.Polite
                        }
                    )
                }

                Button(
                    onClick = {
                        if (orderedStart != null && orderedEnd != null) {
                            onMonthRangeSelectionChange(orderedStart, orderedEnd)
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

@Composable
private fun MonthButton(
    yearMonth: YearMonth,
    isInRange: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    colors: MonthRangePickerColors
) {
    val backgroundColor = when {
        isSelected -> colors.selectedMonthContainerColor
        isInRange -> colors.monthInRangeContainerColor
        else -> colors.monthContainerColor
    }

    val contentColor = when {
        isSelected -> colors.selectedMonthContentColor
        isInRange -> colors.monthInRangeContentColor
        else -> colors.monthContentColor
    }

    val monthName = MonthNames[yearMonth.month - 1]

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(72.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .semantics {
                contentDescription = if (isSelected) {
                    "Selected month $monthName ${yearMonth.year}"
                } else if (isInRange) {
                    "Month $monthName ${yearMonth.year} in range"
                } else {
                    "Month $monthName ${yearMonth.year}"
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = monthName,
            color = contentColor,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MonthEntryContainer(
    modifier: Modifier,
    title: (@Composable () -> Unit)?,
    headline: (@Composable () -> Unit)?,
    headlineTextStyle: androidx.compose.ui.text.TextStyle,
    headerMinHeight: androidx.compose.ui.unit.Dp,
    colors: MonthRangePickerColors,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .background(colors.containerColor)
    ) {
        if (title != null) {
            Box(modifier = Modifier.padding(MonthRangePickerTitlePadding)) {
                title()
            }
        }

        if (headline != null) {
            Box(
                modifier = Modifier
                    .padding(MonthRangePickerHeadlinePadding)
                    .fillMaxWidth()
            ) {
                headline()
            }
        }

        content()
    }
}

object MonthRangePickerDefaults {
    val YearRange: IntRange = IntRange(2000, 2999)
    val MonthNames = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surface,
        titleContentColor: Color = MaterialTheme.colorScheme.onSurface,
        headlineContentColor: Color = MaterialTheme.colorScheme.onSurface,
        monthContentColor: Color = MaterialTheme.colorScheme.onSurface,
        monthInRangeContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
        selectedMonthContentColor: Color = MaterialTheme.colorScheme.onPrimary,
        monthContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        monthInRangeContainerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        selectedMonthContainerColor: Color = MaterialTheme.colorScheme.primary
    ): MonthRangePickerColors = MonthRangePickerColors(
        containerColor = containerColor,
        titleContentColor = titleContentColor,
        headlineContentColor = headlineContentColor,
        monthContentColor = monthContentColor,
        monthInRangeContentColor = monthInRangeContentColor,
        selectedMonthContentColor = selectedMonthContentColor,
        monthContainerColor = monthContainerColor,
        monthInRangeContainerColor = monthInRangeContainerColor,
        selectedMonthContainerColor = selectedMonthContainerColor
    )

    @Composable
    fun MonthRangePickerTitle(
        modifier: Modifier = Modifier
    ) {
        Text(
            text = "Select Month Range",
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier
        )
    }

    @Composable
    fun MonthRangePickerHeadline(
        selectedStartYearMonth: YearMonth?,
        selectedEndYearMonth: YearMonth?,
        modifier: Modifier = Modifier
    ) {
        val headlineText = when {
            selectedStartYearMonth == null && selectedEndYearMonth == null -> "No selection"
            selectedStartYearMonth != null && selectedEndYearMonth == null -> "Start: ${selectedStartYearMonth.month}/${selectedStartYearMonth.year}"
            else -> "Range: ${selectedStartYearMonth?.month}/${selectedStartYearMonth?.year} - ${selectedEndYearMonth?.month}/${selectedEndYearMonth?.year}"
        }

        Text(
            text = headlineText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.semantics {
                liveRegion = androidx.compose.ui.semantics.LiveRegionMode.Polite
            }
        )
    }
}

class MonthRangePickerColors(
    val containerColor: Color,
    val titleContentColor: Color,
    val headlineContentColor: Color,
    val monthContentColor: Color,
    val monthInRangeContentColor: Color,
    val selectedMonthContentColor: Color,
    val monthContainerColor: Color,
    val monthInRangeContainerColor: Color,
    val selectedMonthContainerColor: Color
)

private val MonthRangePickerTitlePadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp)
private val MonthRangePickerHeadlinePadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
