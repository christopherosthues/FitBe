package org.darthacheron.fitbe.components.date.week

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.week_range_picker_content_description_current_week
import fitbe.composeapp.generated.resources.week_range_picker_content_description_end_headline
import fitbe.composeapp.generated.resources.week_range_picker_content_description_start_headline
import fitbe.composeapp.generated.resources.week_range_picker_content_description_week_in_range
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.jetbrains.compose.resources.stringResource

data class YearWeek(val year: Int, val week: Int) : Comparable<YearWeek> {
    init {
        require(year in 1..9999) { "Year must be between 1 and 9999" }
        require(week in 1..53) { "Week must be between 1 and 53" }
    }

    fun weeksUntil(other: YearWeek): Int {
        return (other.year - this.year) * 52 + (other.week - this.week)
    }

    fun startDateMillis(): Long {
        val date = LocalDate.fromYearWeek(year, week, DayOfWeek.MONDAY)
        return date.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
    }

    fun endDateMillis(): Long {
        val date = LocalDate.fromYearWeek(year, week, DayOfWeek.MONDAY)
        val endDate = date.plus(DatePeriod(days = 6))
        val endDateTime = endDate.atTime(23, 59, 59, 999)
        return endDateTime.toInstant(TimeZone.UTC).toEpochMilliseconds()
    }

    override fun compareTo(other: YearWeek): Int {
        return when {
            this.year != other.year -> this.year.compareTo(other.year)
            else -> this.week.compareTo(other.week)
        }
    }

    override fun toString(): String = "$year-W$week"
}

fun LocalDate.Companion.fromYearWeek(year: Int, week: Int, dayOfWeek: DayOfWeek): LocalDate {
    val firstDayOfYear = LocalDate(year, 1, 1)
    val firstThursday = if (firstDayOfYear.dayOfWeek <= DayOfWeek.THURSDAY) {
        firstDayOfYear.plus(DatePeriod(days = DayOfWeek.THURSDAY.isoDayNumber - firstDayOfYear.dayOfWeek.isoDayNumber))
    } else {
        firstDayOfYear.plus(DatePeriod(days = 7 - (firstDayOfYear.dayOfWeek.isoDayNumber - DayOfWeek.THURSDAY.isoDayNumber)))
    }
    val daysOffset = (week - 1) * 7 + (dayOfWeek.isoDayNumber - 1) - DayOfWeek.THURSDAY.isoDayNumber + 1
    return firstThursday.plus(DatePeriod(days = daysOffset))
}

class WeekRangePickerStateImpl(
    initialSelectedStartWeek: YearWeek?,
    initialSelectedEndWeek: YearWeek?,
    override val yearRange: IntRange,
    override val selectableWeeks: SelectableWeeks,
) : WeekRangePickerState {
    override var selectedStartWeek: YearWeek? by mutableStateOf(initialSelectedStartWeek)
    override var selectedEndWeek: YearWeek? by mutableStateOf(initialSelectedEndWeek)

    override fun setSelection(startWeek: YearWeek?, endWeek: YearWeek?) {
        if (startWeek != null && endWeek != null) {
            require(startWeek <= endWeek) { "Start week must be before or equal to end week" }
            require(selectableWeeks.isWeekSelectable(startWeek)) { "Start week is not selectable" }
            require(selectableWeeks.isWeekSelectable(endWeek)) { "End week is not selectable" }
        }
        selectedStartWeek = startWeek
        selectedEndWeek = endWeek
    }
}

interface SelectableWeeks {
    fun isWeekSelectable(yearWeek: YearWeek): Boolean = true
}

interface WeekRangePickerState {
    val selectedStartWeek: YearWeek?
    val selectedEndWeek: YearWeek?
    val yearRange: IntRange
    val selectableWeeks: SelectableWeeks
    fun setSelection(startWeek: YearWeek?, endWeek: YearWeek?)
}

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
            selectedStartYearWeek = state.selectedStartWeek,
            selectedEndYearWeek = state.selectedEndWeek,
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
            selectedStartYearWeek = state.selectedStartWeek,
            selectedEndYearWeek = state.selectedEndWeek,
            onWeekRangeSelectionChange = { startYearWeek, endYearWeek ->
                try {
                    state.setSelection(startYearWeek, endYearWeek)
                } catch (ex: IllegalArgumentException) {
                    // Just ignore it
                }
            },
            yearRange = state.yearRange,
            colors = colors,
            selectableWeeks = state.selectableWeeks,
        )
    }
}

@Composable
fun rememberWeekRangePickerState(
    initialSelectedStartYearWeek: YearWeek? = null,
    initialSelectedEndYearWeek: YearWeek? = null,
    yearRange: IntRange = WeekRangePickerDefaults.YearRange,
    selectableWeeks: SelectableWeeks = WeekRangePickerDefaults.AllDates,
): WeekRangePickerState {
    return remember {
        WeekRangePickerStateImpl(
            initialSelectedStartWeek = initialSelectedStartYearWeek,
            initialSelectedEndWeek = initialSelectedEndYearWeek,
            yearRange = yearRange,
            selectableWeeks = selectableWeeks
        )
    }
}

@Composable
private fun WeekRangePickerContent(
    selectedStartYearWeek: YearWeek?,
    selectedEndYearWeek: YearWeek?,
    onWeekRangeSelectionChange: (startYearWeek: YearWeek?, endYearWeek: YearWeek?) -> Unit,
    yearRange: IntRange,
    colors: WeekRangePickerColors,
    selectableWeeks: SelectableWeeks,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val sortedYears = yearRange.toList().sorted()

    // Calculate the index of the current month
    val currentYearWeek =
        Clock.System.now().toLocalDateTime(TimeZone.UTC).date.isoWeekAndYear().let {
            YearWeek(it.first, it.second)
        }
    val currentYearWeekIndex = sortedYears.indexOf(currentYearWeek.year)

    val orderedStart = if (selectedStartYearWeek != null && selectedEndYearWeek != null)
        minOf(selectedStartYearWeek, selectedEndYearWeek) else null
    val orderedEnd = if (selectedStartYearWeek != null && selectedEndYearWeek != null)
        maxOf(selectedStartYearWeek, selectedEndYearWeek) else null

    ProvideTextStyle(value = MaterialTheme.typography.bodyLarge) {
        Column(modifier = Modifier.padding(16.dp)) {
            LazyColumn(state = listState) {
                items(sortedYears) { year ->
                    Column {
                        Text(
                            text = "$year",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        val weeksInYear = getWeeksInYear(year)
                        val chunkedWeeks = weeksInYear.chunked(ChunkedWeeks)

                        chunkedWeeks.forEach { weekRow ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                weekRow.forEach { weekNum ->
                                    val yw = YearWeek(year, weekNum)
                                    val isInRange =
                                        orderedStart != null && orderedEnd != null && yw in orderedStart..orderedEnd


                                    val startYearWeekSelected = yw == selectedStartYearWeek
                                    val endYearWeekSelected = yw == selectedEndYearWeek
                                    val isSelected =
                                        startYearWeekSelected || endYearWeekSelected
                                    val isCurrentYearWeek = currentYearWeek == yw
                                    val dateInMillis = yw.startDateMillis()
                                    val weekContentDescription =
                                        weekContentDescription(
                                            isCurrentYearWeek = isCurrentYearWeek,
                                            isStartYearWeek = startYearWeekSelected,
                                            isEndYearWeek = endYearWeekSelected,
                                            isInRange = isInRange
                                        )
                                    val formattedDateDescription = formatYearWeek(yw)

                                    WeekButton(
                                        yearWeek = yw,
                                        modifier = Modifier,
                                        selected = isSelected,
                                        onClick = {
                                            if (selectedStartYearWeek == null || selectedEndYearWeek != null) {
                                                onWeekRangeSelectionChange(yw, null)
                                            } else {
                                                onWeekRangeSelectionChange(
                                                    selectedStartYearWeek,
                                                    yw
                                                )
                                            }
                                        },
                                        // Only animate on the first selected day. This is important to
                                        // disable when drawing a range marker behind the days on an
                                        // end-date selection.
                                        animateChecked = startYearWeekSelected,
                                        enabled =
                                            remember(dateInMillis, selectableWeeks) {
                                                // Disabled a day in case its year is not selectable, or the
                                                // date itself is specifically not allowed by the state's
                                                // SelectableDates.
                                                with(selectableWeeks) {
                                                    isWeekSelectable(yw)
                                                }
                                            },
                                        isCurrentYearWeek = isCurrentYearWeek,
                                        inRange = isInRange,
                                        description =
                                            if (weekContentDescription != null) {
                                                "$weekContentDescription, $formattedDateDescription"
                                            } else {
                                                formattedDateDescription
                                            },
                                        colors = colors
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Auto-scroll to the current month when the composable is first launched
    LaunchedEffect(Unit) {
        if (currentYearWeekIndex != -1) {
            coroutineScope.launch {
                listState.scrollToItem(currentYearWeekIndex)
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

fun formatYearWeek(yearWeek: YearWeek): String {
    return "Week ${yearWeek.week} ${yearWeek.year}"
}

@Composable
private fun weekContentDescription(
    isCurrentYearWeek: Boolean,
    isStartYearWeek: Boolean,
    isEndYearWeek: Boolean,
    isInRange: Boolean
): String? {
    val descriptionBuilder = StringBuilder()
    when {
        isStartYearWeek ->
            descriptionBuilder.append(stringResource(Res.string.week_range_picker_content_description_start_headline))

        isEndYearWeek ->
            descriptionBuilder.append(stringResource(Res.string.week_range_picker_content_description_end_headline))

        isInRange ->
            descriptionBuilder.append(stringResource(Res.string.week_range_picker_content_description_week_in_range))
    }
    if (isCurrentYearWeek) {
        if (descriptionBuilder.isNotEmpty()) descriptionBuilder.append(", ")
        descriptionBuilder.append(stringResource(Res.string.week_range_picker_content_description_current_week))
    }
    return if (descriptionBuilder.isEmpty()) null else descriptionBuilder.toString()
}

@Composable
private fun WeekButton(
    yearWeek: YearWeek,
    modifier: Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    animateChecked: Boolean,
    enabled: Boolean,
    isCurrentYearWeek: Boolean,
    inRange: Boolean,
    description: String,
    colors: WeekRangePickerColors
) {
    val backgroundModifier = if (inRange && !selected) {
        Modifier
            .background(
                color = colors.weekInRangeContainerColor,
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
                .semantics(mergeDescendants = true) {
                    text = AnnotatedString(description)
                    role = Role.Button
                }
                .requiredSize(RecommendedSizeForAccessibility),
        enabled = enabled,
        shape = CircleShape,
        color =
            colors
                .weekContainerColor(
                    selected = selected,
                    enabled = enabled,
                    animate = animateChecked
                )
                .value,
        contentColor =
            colors
                .weekContentColor(
                    isCurrentWeek = isCurrentYearWeek,
                    selected = selected,
                    inRange = inRange,
                    enabled = enabled,
                )
                .value,
        border =
            if (isCurrentYearWeek && !selected) {
                BorderStroke(
                    WeekRangePickerModalTokens.WeekTodayContainerOutlineWidth,
                    colors.currentWeekBorderColor
                )
            } else {
                null
            }
    ) {
        Box(
            modifier =
                Modifier.requiredSize(
                    WeekRangePickerModalTokens.WeekStateLayerWidth,
                    WeekRangePickerModalTokens.WeekStateLayerHeight
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "W${yearWeek.week}",
                textAlign = TextAlign.Center,
                modifier = Modifier.clearAndSetSemantics {}
            )
        }
    }
}

internal object WeekRangePickerModalTokens {
    val WeekStateLayerHeight = 40.0.dp
    val WeekStateLayerWidth = 40.0.dp
    val WeekTodayContainerOutlineWidth = 1.0.dp
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
            .defaultMinSize(minHeight = headerMinHeight)
            .background(colors.containerColor)
    ) {
        if (title != null) {
            Box(modifier = Modifier.padding(WeekRangePickerTitlePadding)) {
                title()
            }
        }

        if (headline != null) {
            ProvideTextStyle(value = headlineTextStyle) {
                Box(
                    modifier = Modifier
                        .padding(WeekRangePickerHeadlinePadding)
                        .fillMaxWidth()
                ) {
                    headline()
                }
            }
        }

        content()
    }
}

object WeekRangePickerDefaults {
    val YearRange: IntRange = IntRange(2000, 2999)

    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
        titleContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        headlineContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        weekContentColor: Color = MaterialTheme.colorScheme.onSurface,
        weekInRangeContentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        selectedWeekContentColor: Color = MaterialTheme.colorScheme.onPrimary,
        disabledSelectedWeekContentColor: Color = MaterialTheme.colorScheme.onPrimary.copy(alpha = DisabledAlpha),
        weekContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        weekInRangeContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        disabledWeekContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = DisabledAlpha),
        selectedWeekContainerColor: Color = MaterialTheme.colorScheme.primary,
        currentWeekContentColor: Color = MaterialTheme.colorScheme.primary,
        currentWeekBorderColor: Color = MaterialTheme.colorScheme.primary,
        disabledSelectedWeekContainerColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = DisabledAlpha),
    ): WeekRangePickerColors = WeekRangePickerColors(
        containerColor = containerColor,
        titleContentColor = titleContentColor,
        headlineContentColor = headlineContentColor,
        weekContentColor = weekContentColor,
        weekInSelectionRangeContentColor = weekInRangeContentColor,
        selectedWeekContentColor = selectedWeekContentColor,
        weekContainerColor = weekContainerColor,
        weekInRangeContainerColor = weekInRangeContainerColor,
        selectedWeekContainerColor = selectedWeekContainerColor,
        currentWeekContentColor = currentWeekContentColor,
        disabledSelectedWeekContentColor = disabledSelectedWeekContentColor,
        disabledWeekContentColor = disabledWeekContentColor,
        currentWeekBorderColor = currentWeekBorderColor,
        disabledSelectedWeekContainerColor = disabledSelectedWeekContainerColor,
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

    /** A default [SelectableWeeks] that allows all dates to be selected. */
    val AllDates: SelectableWeeks = object : SelectableWeeks {}
}

class WeekRangePickerColors(
    val containerColor: Color,
    val titleContentColor: Color,
    val headlineContentColor: Color,
    val weekContentColor: Color,
    val weekInSelectionRangeContentColor: Color,
    val selectedWeekContentColor: Color,
    val weekContainerColor: Color,
    val weekInRangeContainerColor: Color,
    val selectedWeekContainerColor: Color,
    val currentWeekContentColor: Color,
    val currentWeekBorderColor: Color,
    val disabledSelectedWeekContentColor: Color,
    val disabledWeekContentColor: Color,
    val disabledSelectedWeekContainerColor: Color,
) {
    @Composable
    internal fun weekContentColor(
        isCurrentWeek: Boolean,
        selected: Boolean,
        inRange: Boolean,
        enabled: Boolean
    ): State<Color> {
        val target =
            when {
                selected && enabled -> selectedWeekContentColor
                selected && !enabled -> disabledSelectedWeekContentColor
                inRange && enabled -> weekInSelectionRangeContentColor
                inRange && !enabled -> disabledWeekContentColor
                isCurrentWeek -> currentWeekContentColor
                enabled -> weekContentColor
                else -> disabledWeekContentColor
            }

        return if (inRange) {
            rememberUpdatedState(target)
        } else {
            // Animate the content color only when the day is not in a range.
            animateColorAsState(target, tween(durationMillis = DurationShort2.toInt()))
        }
    }

    @Composable
    internal fun weekContainerColor(
        selected: Boolean,
        enabled: Boolean,
        animate: Boolean
    ): State<Color> {
        val target =
            if (selected) {
                if (enabled) selectedWeekContainerColor else disabledSelectedWeekContainerColor
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

private val WeekRangePickerTitlePadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp)
private val WeekRangePickerHeadlinePadding =
    PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
internal val RecommendedSizeForAccessibility = 48.dp
const val DurationShort2 = 100.0
internal const val DisabledAlpha = 0.38f
private const val ChunkedWeeks = 4