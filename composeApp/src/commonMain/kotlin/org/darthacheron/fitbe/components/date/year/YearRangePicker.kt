package org.darthacheron.fitbe.components.date.year

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.year_range_picker_content_description_current_year
import fitbe.composeapp.generated.resources.year_range_picker_content_description_end_headline
import fitbe.composeapp.generated.resources.year_range_picker_content_description_start_headline
import fitbe.composeapp.generated.resources.year_range_picker_content_description_year_in_range
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

data class Year(val value: Int) : Comparable<Year> {
    init {
        require(value in 1..9999) { "Year must be between 1 and 9999" }
    }

    fun until(other: Year): Int = other.value - this.value

    fun startDateMillis(): Long {
        return LocalDate(value, 1, 1).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
    }

    fun endDateMillis(): Long {
        return LocalDateTime(value, 12, 31, 23, 59, 59, 999).toInstant(TimeZone.UTC)
            .toEpochMilliseconds()
    }

    override fun compareTo(other: Year): Int = value.compareTo(other.value)
    override fun toString(): String = value.toString()
}

class YearRangePickerStateImpl(
    initialSelectedStartYear: Year?,
    initialSelectedEndYear: Year?,
    override val yearRange: IntRange,
    override val selectableYears: SelectableYears,
) : YearRangePickerState {
    override var selectedStartYear: Year? by mutableStateOf(initialSelectedStartYear)
    override var selectedEndYear: Year? by mutableStateOf(initialSelectedEndYear)

    override fun setSelection(startYear: Year?, endYear: Year?) {
        if (startYear != null && endYear != null) {
            require(startYear <= endYear) { "Start year must be before or equal to end year" }
            require(selectableYears.isYearSelectable(startYear)) { "Start year is not selectable" }
            require(selectableYears.isYearSelectable(endYear)) { "End year is not selectable" }
        }
        selectedStartYear = startYear
        selectedEndYear = endYear
    }
}

interface SelectableYears {
    fun isYearSelectable(year: Year): Boolean = true
}

interface YearRangePickerState {
    val selectedStartYear: Year?
    val selectedEndYear: Year?
    val yearRange: IntRange
    val selectableYears: SelectableYears
    fun setSelection(startYear: Year?, endYear: Year?)
}

@OptIn(ExperimentalMaterial3Api::class)
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
    colors: YearRangePickerColors = YearRangePickerDefaults.colors(),
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
                try {
                    state.setSelection(startYear, endYear)
                } catch (ex: IllegalArgumentException) {
                    // Just ignore it
                }
            },
            yearRange = state.yearRange,
            colors = colors,
            selectableYears = state.selectableYears
        )
    }
}


@Composable
fun rememberYearRangePickerState(
    initialSelectedStartYear: Year? = null,
    initialSelectedEndYear: Year? = null,
    yearRange: IntRange = YearRangePickerDefaults.YearRange,
    selectableYears: SelectableYears = YearRangePickerDefaults.AllDates
): YearRangePickerState {
    return remember {
        YearRangePickerStateImpl(
            initialSelectedStartYear = initialSelectedStartYear,
            initialSelectedEndYear = initialSelectedEndYear,
            yearRange = yearRange,
            selectableYears = selectableYears
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YearRangePickerContent(
    selectedStartYear: Year?,
    selectedEndYear: Year?,
    onYearRangeSelectionChange: (startYear: Year?, endYear: Year?) -> Unit,
    yearRange: IntRange,
    colors: YearRangePickerColors,
    selectableYears: SelectableYears,
) {
    val allYears = yearRange.map { Year(it) }
    val chunkedYears = allYears.chunked(ChunkedYears)
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Calculate the index of the current year
    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.year
    val currentYearIndex = allYears.indexOfFirst { it.value == currentYear }

    val orderedStart = if (selectedStartYear != null && selectedEndYear != null)
        minOf(selectedStartYear, selectedEndYear) else null
    val orderedEnd = if (selectedStartYear != null && selectedEndYear != null)
        maxOf(selectedStartYear, selectedEndYear) else null

    ProvideTextStyle(value = MaterialTheme.typography.bodyLarge) {
        Column(modifier = Modifier.padding(16.dp)) {
            LazyColumn(state = listState) {
                items(chunkedYears) { rowYears ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        rowYears.fastForEach { year ->
                            val isInRange =
                                orderedStart != null && orderedEnd != null && year in orderedStart..orderedEnd
                            val startYearSelected = year == selectedStartYear
                            val endYearSelected = year == selectedEndYear
                            val isSelected = year == selectedStartYear || year == selectedEndYear
                            val isCurrentYear = currentYear == year.value
                            val dateInMillis = year.startDateMillis()
                            val yearContentDescription =
                                yearContentDescription(
                                    isCurrentYear = isCurrentYear,
                                    isStartYear = startYearSelected,
                                    isEndYear = endYearSelected,
                                    isInRange = isInRange
                                )
                            val formattedDateDescription = formatYear(currentYear)

                            YearButton(
                                year = year,
                                modifier = Modifier,
                                selected = isSelected,
                                onClick = {
                                    if (selectedStartYear == null || selectedEndYear != null) {
                                        onYearRangeSelectionChange(year, null)
                                    } else {
                                        onYearRangeSelectionChange(selectedStartYear, year)
                                    }
                                },
                                // Only animate on the first selected day. This is important to
                                // disable when drawing a range marker behind the days on an
                                // end-date selection.
                                animateChecked = startYearSelected,
                                enabled =
                                    remember(dateInMillis, selectableYears) {
                                        // Disabled a day in case its year is not selectable, or the
                                        // date itself is specifically not allowed by the state's
                                        // SelectableDates.
                                        with(selectableYears) {
                                            isYearSelectable(year)
                                        }
                                    },
                                isCurrentYear = isCurrentYear,
                                inRange = isInRange,
                                description =
                                    if (yearContentDescription != null) {
                                        "$yearContentDescription, $formattedDateDescription"
                                    } else {
                                        formattedDateDescription
                                    },
                                colors = colors
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    // Auto-scroll to the current year when the composable is first launched
    LaunchedEffect(Unit) {
        if (currentYearIndex != -1) {
            coroutineScope.launch {
                listState.scrollToItem(currentYearIndex / ChunkedYears)
            }
        }
    }
}

fun formatYear(year: Int): String {
    return year.toString()
}

@Composable
private fun yearContentDescription(
    isCurrentYear: Boolean,
    isStartYear: Boolean,
    isEndYear: Boolean,
    isInRange: Boolean
): String? {
    val descriptionBuilder = StringBuilder()
    when {
        isStartYear ->
            descriptionBuilder.append(stringResource(Res.string.year_range_picker_content_description_start_headline))
        isEndYear ->
            descriptionBuilder.append(stringResource(Res.string.year_range_picker_content_description_end_headline))
        isInRange ->
            descriptionBuilder.append(stringResource(Res.string.year_range_picker_content_description_year_in_range))
    }
    if (isCurrentYear) {
        if (descriptionBuilder.isNotEmpty()) descriptionBuilder.append(", ")
        descriptionBuilder.append(stringResource(Res.string.year_range_picker_content_description_current_year))
    }
    return if (descriptionBuilder.isEmpty()) null else descriptionBuilder.toString()
}

@Composable
private fun YearButton(
    modifier: Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    animateChecked: Boolean,
    enabled: Boolean,
    isCurrentYear: Boolean,
    inRange: Boolean,
    description: String,
    colors: YearRangePickerColors,
    year: Year,
) {
    val backgroundModifier = if (inRange && !selected) {
        Modifier
            .background(
                color = colors.yearInRangeContainerColor,
                shape = RectangleShape
            )
    } else {
        Modifier
    }

    Surface(
        selected = selected,
        onClick = onClick,
        modifier =
            modifier
                .then(backgroundModifier)
                // Apply and merge semantics here. This will ensure that when scrolling the list the
                // entire Day surface is treated as one unit and holds the date semantics even when
                // it's
                // not completely visible atm.
                .semantics(mergeDescendants = true) {
                    text = AnnotatedString(description)
                    role = Role.Button
                }
                .requiredSize(RecommendedSizeForAccessibility),
        enabled = enabled,
        shape = CircleShape,
        color =
            colors
                .yearContainerColor(selected = selected, enabled = enabled, animate = animateChecked)
                .value,
        contentColor =
            colors
                .yearContentColor(
                    isCurrentYear = isCurrentYear,
                    selected = selected,
                    inRange = inRange,
                    enabled = enabled,
                )
                .value,
        border =
            if (isCurrentYear && !selected) {
                BorderStroke(
                    YearRangePickerModalTokens.DateTodayContainerOutlineWidth,
                    colors.currentYearBorderColor
                )
            } else {
                null
            }
    ) {
        Box(
            modifier =
                Modifier.requiredSize(
                    YearRangePickerModalTokens.YearStateLayerWidth,
                    YearRangePickerModalTokens.YearStateLayerHeight
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = year.toString(),
                textAlign = TextAlign.Center,
                // The semantics are set at the Day level.
                modifier = Modifier.clearAndSetSemantics {},
            )
        }
    }
}

internal object YearRangePickerModalTokens {
    val YearStateLayerHeight = 40.0.dp
    val YearStateLayerWidth = 40.0.dp
    val DateTodayContainerOutlineWidth = 1.0.dp
}


@Composable
private fun YearEntryContainer(
    modifier: Modifier,
    title: (@Composable () -> Unit)?,
    headline: (@Composable () -> Unit)?,
    headlineTextStyle: TextStyle,
    headerMinHeight: Dp,
    colors: YearRangePickerColors,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .defaultMinSize(minHeight = headerMinHeight)
            .background(colors.containerColor)
    ) {
        if (title != null) {
            Box(modifier = Modifier.padding(YearRangePickerTitlePadding)) {
                title()
            }
        }

        if (headline != null) {
            ProvideTextStyle(value = headlineTextStyle) {
                Box(
                    modifier = Modifier
                        .padding(YearRangePickerHeadlinePadding)
                        .fillMaxWidth()
                ) {
                    headline()
                }
            }
        }

        content()
    }
}

object YearRangePickerDefaults {
    val YearRange: IntRange = IntRange(2000, 2999)

    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
        titleContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        headlineContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        yearContentColor: Color = MaterialTheme.colorScheme.onSurface,
        yearInRangeContentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        selectedYearContentColor: Color = MaterialTheme.colorScheme.onPrimary,
        disabledSelectedYearContentColor: Color = MaterialTheme.colorScheme.onPrimary.copy(alpha = DisabledAlpha),
        yearContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant, // TODO
        yearInRangeContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        disabledYearContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = DisabledAlpha),
        selectedYearContainerColor: Color = MaterialTheme.colorScheme.primary,
        currentYearContentColor: Color = MaterialTheme.colorScheme.primary,
        currentYearBorderColor: Color = MaterialTheme.colorScheme.primary,
        disabledSelectedYearContainerColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = DisabledAlpha),
    ): YearRangePickerColors = YearRangePickerColors(
        containerColor = containerColor,
        titleContentColor = titleContentColor,
        headlineContentColor = headlineContentColor,
        yearContentColor = yearContentColor,
        yearInSelectionRangeContentColor = yearInRangeContentColor,
        selectedYearContentColor = selectedYearContentColor,
        yearContainerColor = yearContainerColor,
        yearInRangeContainerColor = yearInRangeContainerColor,
        selectedYearContainerColor = selectedYearContainerColor,
        currentYearContentColor = currentYearContentColor,
        disabledSelectedYearContentColor = disabledSelectedYearContentColor,
        disabledYearContentColor = disabledYearContentColor,
        currentYearBorderColor = currentYearBorderColor,
        disabledSelectedYearContainerColor = disabledSelectedYearContainerColor,
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
                liveRegion = LiveRegionMode.Polite
            }
        )
    }

    /** A default [SelectableYears] that allows all dates to be selected. */
    val AllDates: SelectableYears = object : SelectableYears {}
}

class YearRangePickerColors(
    val containerColor: Color,
    val titleContentColor: Color,
    val headlineContentColor: Color,
    val yearContentColor: Color,
    val yearInSelectionRangeContentColor: Color,
    val selectedYearContentColor: Color,
    val yearContainerColor: Color,
    val yearInRangeContainerColor: Color,
    val selectedYearContainerColor: Color,
    val currentYearContentColor: Color,
    val currentYearBorderColor: Color,
    val disabledSelectedYearContentColor: Color,
    val disabledYearContentColor: Color,
    val disabledSelectedYearContainerColor: Color,
) {
    @Composable
    internal fun yearContentColor(
        isCurrentYear: Boolean,
        selected: Boolean,
        inRange: Boolean,
        enabled: Boolean
    ): State<Color> {
        val target =
            when {
                selected && enabled -> selectedYearContentColor
                selected && !enabled -> disabledSelectedYearContentColor
                inRange && enabled -> yearInSelectionRangeContentColor
                inRange && !enabled -> disabledYearContentColor
                isCurrentYear -> currentYearContentColor
                enabled -> yearContentColor
                else -> disabledYearContentColor
            }

        return if (inRange) {
            rememberUpdatedState(target)
        } else {
            // Animate the content color only when the day is not in a range.
            animateColorAsState(target, tween(durationMillis = DurationShort2.toInt()))
        }
    }

    @Composable
    internal fun yearContainerColor(
        selected: Boolean,
        enabled: Boolean,
        animate: Boolean
    ): State<Color> {
        val target =
            if (selected) {
                if (enabled) selectedYearContainerColor else disabledSelectedYearContainerColor
            } else {
                Color.Transparent
            }
        return if (animate) {
            animateColorAsState(target, tween(durationMillis = DurationShort2.toInt()))
        } else {
            rememberUpdatedState(target)
        }
    }
}

private val YearRangePickerTitlePadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp)
private val YearRangePickerHeadlinePadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
internal val RecommendedSizeForAccessibility = 48.dp

const val DurationShort2 = 100.0
internal const val DisabledAlpha = 0.38f
private const val ChunkedYears = 5