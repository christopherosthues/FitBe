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
import androidx.compose.ui.util.fastForEach

@Composable
fun YearRangePicker(
    state: YearRangePickerState,
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = {
        YearRangePickerDefaults.YearRangePickerTitle(
            modifier = Modifier.padding(YearRangePickerTitlePadding)
        )
    },
    headline: (@Composable () -> Unit)? = {
        YearRangePickerDefaults.YearRangePickerHeadline(
            selectedStartYear = state.selectedStartYear,
            selectedEndYear = state.selectedEndYear,
            modifier = Modifier.padding(YearRangePickerHeadlinePadding)
        )
    },
    colors: YearRangePickerColors = YearRangePickerDefaults.colors()
) {
    YearEntryContainer(
        modifier = modifier,
        title = title,
        headline = headline,
        headlineTextStyle = MaterialTheme.typography.bodyMedium,
        headerMinHeight = 100.dp,
        colors = colors,
    ) {
        YearRangePickerContent(
            selectedStartYear = state.selectedStartYear,
            selectedEndYear = state.selectedEndYear,
            onYearRangeSelectionChange = { startYear, endYear ->
                state.setSelection(startYear, endYear)
            },
            yearRange = state.yearRange,
            colors = colors
        )
    }
}

@Composable
fun rememberYearRangePickerState(
    initialSelectedStartYear: Year? = null,
    initialSelectedEndYear: Year? = null,
    yearRange: IntRange = YearRangePickerDefaults.YearRange
): YearRangePickerState {
    return remember {
        YearRangePickerStateImpl(
            initialSelectedStartYear = initialSelectedStartYear,
            initialSelectedEndYear = initialSelectedEndYear,
            yearRange = yearRange
        )
    }
}

interface YearRangePickerState {
    val selectedStartYear: Year?
    val selectedEndYear: Year?
    val yearRange: IntRange
    fun setSelection(startYear: Year?, endYear: Year?)
}

class YearRangePickerStateImpl(
    initialSelectedStartYear: Year?,
    initialSelectedEndYear: Year?,
    override val yearRange: IntRange
) : YearRangePickerState {
    override var selectedStartYear: Year? by mutableStateOf(initialSelectedStartYear)
    override var selectedEndYear: Year? by mutableStateOf(initialSelectedEndYear)

    override fun setSelection(startYear: Year?, endYear: Year?) {
        if (startYear != null && endYear != null) {
            require(startYear <= endYear) { "Start year must be before or equal to end year" }
            require((endYear.value - startYear.value) <= 6) { "Range must be 6 years or less" }
        }
        selectedStartYear = startYear
        selectedEndYear = endYear
    }
}

@Composable
private fun YearRangePickerContent(
    selectedStartYear: Year?,
    selectedEndYear: Year?,
    onYearRangeSelectionChange: (startYear: Year?, endYear: Year?) -> Unit,
    yearRange: IntRange,
    colors: YearRangePickerColors
) {
    val allYears = yearRange.map { Year(it) }
    val chunkedYears = allYears.chunked(4)
    val listState = rememberLazyListState()

    val isComplete = selectedStartYear != null && selectedEndYear != null
    val orderedStart = if (selectedStartYear != null && selectedEndYear != null)
        minOf(selectedStartYear, selectedEndYear) else null
    val orderedEnd = if (selectedStartYear != null && selectedEndYear != null)
        maxOf(selectedStartYear, selectedEndYear) else null
    val rangeLength = if (orderedStart != null && orderedEnd != null)
        orderedStart.until(orderedEnd) else 0
    val isValid = isComplete && rangeLength <= 6

    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn(state = listState) {
            items(chunkedYears) { rowYears ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowYears.fastForEach { year ->
                        val isInRange = orderedStart != null && orderedEnd != null && year in orderedStart..orderedEnd
                        val isSelected = year == selectedStartYear || year == selectedEndYear

                        YearButton(
                            year = year,
                            isInRange = isInRange,
                            isSelected = isSelected,
                            onClick = {
                                if (selectedStartYear == null || (selectedStartYear != null && selectedEndYear != null)) {
                                    onYearRangeSelectionChange(year, null)
                                } else {
                                    onYearRangeSelectionChange(selectedStartYear, year)
                                }
                            },
                            colors = colors
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (!isValid && isComplete) {
            Text(
                "Range must be 6 years or less.",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.semantics {
                    liveRegion = androidx.compose.ui.semantics.LiveRegionMode.Polite
                }
            )
        }

        Button(
            onClick = { orderedStart?.let { start -> orderedEnd?.let { end -> onYearRangeSelectionChange(start, end) } } },
            enabled = isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm Range")
        }
    }
}

@Composable
private fun YearButton(
    year: Year,
    isInRange: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    colors: YearRangePickerColors
) {
    val backgroundColor = when {
        isSelected -> colors.selectedYearContainerColor
        isInRange -> colors.yearInRangeContainerColor
        else -> colors.yearContainerColor
    }

    val contentColor = when {
        isSelected -> colors.selectedYearContentColor
        isInRange -> colors.yearInRangeContentColor
        else -> colors.yearContentColor
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
                    "Selected year ${year.value}"
                } else if (isInRange) {
                    "Year ${year.value} in range"
                } else {
                    "Year ${year.value}"
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = year.toString(),
            textAlign = TextAlign.Center,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun YearEntryContainer(
    modifier: Modifier,
    title: (@Composable () -> Unit)?,
    headline: (@Composable () -> Unit)?,
    headlineTextStyle: androidx.compose.ui.text.TextStyle,
    headerMinHeight: androidx.compose.ui.unit.Dp,
    colors: YearRangePickerColors,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .background(colors.containerColor)
    ) {
        if (title != null) {
            Box(modifier = Modifier.padding(YearRangePickerTitlePadding)) {
                title()
            }
        }

        if (headline != null) {
            Box(
                modifier = Modifier
                    .padding(YearRangePickerHeadlinePadding)
                    .fillMaxWidth()
            ) {
                headline()
            }
        }

        content()
    }
}

object YearRangePickerDefaults {
    val YearRange: IntRange = IntRange(1900, 2100)

    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surface,
        titleContentColor: Color = MaterialTheme.colorScheme.onSurface,
        headlineContentColor: Color = MaterialTheme.colorScheme.onSurface,
        yearContentColor: Color = MaterialTheme.colorScheme.onSurface,
        yearInRangeContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
        selectedYearContentColor: Color = MaterialTheme.colorScheme.onPrimary,
        yearContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        yearInRangeContainerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        selectedYearContainerColor: Color = MaterialTheme.colorScheme.primary
    ): YearRangePickerColors = YearRangePickerColors(
        containerColor = containerColor,
        titleContentColor = titleContentColor,
        headlineContentColor = headlineContentColor,
        yearContentColor = yearContentColor,
        yearInRangeContentColor = yearInRangeContentColor,
        selectedYearContentColor = selectedYearContentColor,
        yearContainerColor = yearContainerColor,
        yearInRangeContainerColor = yearInRangeContainerColor,
        selectedYearContainerColor = selectedYearContainerColor
    )

    @Composable
    fun YearRangePickerTitle(
        modifier: Modifier = Modifier
    ) {
        Text(
            text = "Select Year Range",
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier
        )
    }

    @Composable
    fun YearRangePickerHeadline(
        selectedStartYear: Year?,
        selectedEndYear: Year?,
        modifier: Modifier = Modifier
    ) {
        val headlineText = when {
            selectedStartYear == null && selectedEndYear == null -> "No selection"
            selectedStartYear != null && selectedEndYear == null -> "Start: ${selectedStartYear.value}"
            else -> "Range: ${selectedStartYear?.value} - ${selectedEndYear?.value}"
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

class YearRangePickerColors(
    val containerColor: Color,
    val titleContentColor: Color,
    val headlineContentColor: Color,
    val yearContentColor: Color,
    val yearInRangeContentColor: Color,
    val selectedYearContentColor: Color,
    val yearContainerColor: Color,
    val yearInRangeContainerColor: Color,
    val selectedYearContainerColor: Color
)

private val YearRangePickerTitlePadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp)
private val YearRangePickerHeadlinePadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
