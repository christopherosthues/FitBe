package org.darthacheron.fitbe.components.date.month

import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.month_range_picker_april
import fitbe.composeapp.generated.resources.month_range_picker_august
import fitbe.composeapp.generated.resources.month_range_picker_content_description_current_month
import fitbe.composeapp.generated.resources.month_range_picker_content_description_end_headline
import fitbe.composeapp.generated.resources.month_range_picker_content_description_month_in_range
import fitbe.composeapp.generated.resources.month_range_picker_content_description_start_headline
import fitbe.composeapp.generated.resources.month_range_picker_december
import fitbe.composeapp.generated.resources.month_range_picker_february
import fitbe.composeapp.generated.resources.month_range_picker_january
import fitbe.composeapp.generated.resources.month_range_picker_july
import fitbe.composeapp.generated.resources.month_range_picker_june
import fitbe.composeapp.generated.resources.month_range_picker_march
import fitbe.composeapp.generated.resources.month_range_picker_may
import fitbe.composeapp.generated.resources.month_range_picker_november
import fitbe.composeapp.generated.resources.month_range_picker_october
import fitbe.composeapp.generated.resources.month_range_picker_september
import fitbe.composeapp.generated.resources.Res
import org.darthacheron.fitbe.components.date.month.MonthRangePickerDefaults.MonthNames
import org.jetbrains.compose.resources.stringResource

data class YearMonth(
    val year: Int,
    val month: Int
) : Comparable<YearMonth> {
    init {
        require(year in 1..9999) { "Year must be between 1 and 9999" }
        require(month in 1..12) { "Month must be between 1 and 12" }
    }

    fun monthsUntil(other: YearMonth): Int = (other.year - this.year) * 12 + (other.month - this.month)

    fun startDateMillis(): Long =
        LocalDateTime(year, month, 1, 0, 0, 0)
            .toInstant(TimeZone.UTC)
            .toEpochMilliseconds()

    fun endDateMillis(): Long {
        val lastDayOfMonth =
            when (month) {
                1, 3, 5, 7, 8, 10, 12 -> 31
                4, 6, 9, 11 -> 30
                2 -> if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) 29 else 28
                else -> throw IllegalArgumentException("Invalid month: $month")
            }
        return LocalDateTime(year, month, lastDayOfMonth, 23, 59, 59, 999)
            .toInstant(TimeZone.UTC)
            .toEpochMilliseconds()
    }

    override fun compareTo(other: YearMonth): Int =
        when {
            this.year != other.year -> this.year.compareTo(other.year)
            else -> this.month.compareTo(other.month)
        }

    override fun toString(): String = "$year-$month"
}

class MonthRangePickerStateImpl(
    initialSelectedStartYearMonth: YearMonth?,
    initialSelectedEndYearMonth: YearMonth?,
    override val yearRange: IntRange,
    override val selectableMonths: SelectableMonths
) : MonthRangePickerState {
    override var selectedStartMonth: YearMonth? by mutableStateOf(initialSelectedStartYearMonth)
    override var selectedEndMonth: YearMonth? by mutableStateOf(initialSelectedEndYearMonth)

    override fun setSelection(
        startMonth: YearMonth?,
        endMonth: YearMonth?
    ) {
        if (startMonth != null && endMonth != null) {
            require(startMonth <= endMonth) { "Start year month must be before or equal to end year month" }
            require(selectableMonths.isMonthSelectable(startMonth)) { "Start month is not selectable" }
            require(selectableMonths.isMonthSelectable(endMonth)) { "End month is not selectable" }
        }
        selectedStartMonth = startMonth
        selectedEndMonth = endMonth
    }
}

interface SelectableMonths {
    fun isMonthSelectable(yearMonth: YearMonth): Boolean = true
}

interface MonthRangePickerState {
    val selectedStartMonth: YearMonth?
    val selectedEndMonth: YearMonth?
    val yearRange: IntRange
    val selectableMonths: SelectableMonths

    fun setSelection(
        startMonth: YearMonth?,
        endMonth: YearMonth?
    )
}

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
            selectedStartYearMonth = state.selectedStartMonth,
            selectedEndYearMonth = state.selectedEndMonth,
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
        colors = colors
    ) {
        MonthRangePickerContent(
            selectedStartYearMonth = state.selectedStartMonth,
            selectedEndYearMonth = state.selectedEndMonth,
            onMonthRangeSelectionChange = { startYearMonth, endYearMonth ->
                try {
                    state.setSelection(startYearMonth, endYearMonth)
                } catch (ex: IllegalArgumentException) {
                    // Just ignore it
                }
            },
            yearRange = state.yearRange,
            colors = colors,
            selectableMonths = state.selectableMonths
        )
    }
}

@Composable
fun rememberMonthRangePickerState(
    initialSelectedStartYearMonth: YearMonth? = null,
    initialSelectedEndYearMonth: YearMonth? = null,
    yearRange: IntRange = MonthRangePickerDefaults.YearRange,
    selectableMonths: SelectableMonths = MonthRangePickerDefaults.AllDates
): MonthRangePickerState =
    remember {
        MonthRangePickerStateImpl(
            initialSelectedStartYearMonth = initialSelectedStartYearMonth,
            initialSelectedEndYearMonth = initialSelectedEndYearMonth,
            yearRange = yearRange,
            selectableMonths = selectableMonths
        )
    }

@Composable
private fun MonthRangePickerContent(
    selectedStartYearMonth: YearMonth?,
    selectedEndYearMonth: YearMonth?,
    onMonthRangeSelectionChange: (startYearMonth: YearMonth?, endYearMonth: YearMonth?) -> Unit,
    yearRange: IntRange,
    colors: MonthRangePickerColors,
    selectableMonths: SelectableMonths
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val sortedYears = yearRange.toList().sorted()

    // Calculate the index of the current month
    val currentYearMonth =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).let {
            YearMonth(it.year, it.monthNumber)
        }
    val currentYearMonthIndex = sortedYears.indexOf(currentYearMonth.year)

    val orderedStart =
        if (selectedStartYearMonth != null && selectedEndYearMonth != null)
            minOf(selectedStartYearMonth, selectedEndYearMonth)
        else
            null
    val orderedEnd =
        if (selectedStartYearMonth != null && selectedEndYearMonth != null)
            maxOf(selectedStartYearMonth, selectedEndYearMonth)
        else
            null

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

                        val months = (1..12).map { month -> YearMonth(year, month) }
                        val chunkedMonths = months.chunked(CHUNKED_MONTHS)

                        chunkedMonths.forEach { rowMonths ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                rowMonths.forEach { yearMonth ->
                                    val isInRange =
                                        orderedStart != null && orderedEnd != null &&
                                                yearMonth in orderedStart..orderedEnd
                                    val startYearMonthSelected = yearMonth == selectedStartYearMonth
                                    val endYearMonthSelected = yearMonth == selectedEndYearMonth
                                    val isSelected =
                                        yearMonth == selectedStartYearMonth || endYearMonthSelected
                                    val isCurrentYearMonth = currentYearMonth == yearMonth
                                    val dateInMillis = yearMonth.startDateMillis()
                                    val monthContentDescription =
                                        monthContentDescription(
                                            isCurrentYearMonth = isCurrentYearMonth,
                                            isStartYearMonth = startYearMonthSelected,
                                            isEndYearMonth = endYearMonthSelected,
                                            isInRange = isInRange
                                        )
                                    val formattedDateDescription = formatYearMonth(yearMonth)

                                    MonthButton(
                                        yearMonth = yearMonth,
                                        modifier = Modifier,
                                        selected = isSelected,
                                        onClick = {
                                            if (selectedStartYearMonth == null || selectedEndYearMonth != null) {
                                                onMonthRangeSelectionChange(yearMonth, null)
                                            } else {
                                                onMonthRangeSelectionChange(
                                                    selectedStartYearMonth,
                                                    yearMonth
                                                )
                                            }
                                        },
                                        // Only animate on the first selected day. This is important to
                                        // disable when drawing a range marker behind the days on an
                                        // end-date selection.
                                        animateChecked = startYearMonthSelected,
                                        enabled =
                                            remember(dateInMillis, selectableMonths) {
                                                // Disabled a day in case its year is not selectable, or the
                                                // date itself is specifically not allowed by the state's
                                                // SelectableDates.
                                                with(selectableMonths) {
                                                    isMonthSelectable(yearMonth)
                                                }
                                            },
                                        isCurrentYearMonth = isCurrentYearMonth,
                                        inRange = isInRange,
                                        description =
                                            if (monthContentDescription != null) {
                                                "$monthContentDescription, $formattedDateDescription"
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
        if (currentYearMonthIndex != -1) {
            coroutineScope.launch {
                listState.scrollToItem(currentYearMonthIndex)
            }
        }
    }
}

@Composable
fun formatYearMonth(yearMonth: YearMonth): String =
    "${stringResource(MonthNames[yearMonth.month - 1])} ${yearMonth.year}"

@Composable
private fun monthContentDescription(
    isCurrentYearMonth: Boolean,
    isStartYearMonth: Boolean,
    isEndYearMonth: Boolean,
    isInRange: Boolean
): String? {
    val descriptionBuilder = StringBuilder()
    when {
        isStartYearMonth ->
            descriptionBuilder.append(stringResource(Res.string.month_range_picker_content_description_start_headline))

        isEndYearMonth ->
            descriptionBuilder.append(stringResource(Res.string.month_range_picker_content_description_end_headline))

        isInRange ->
            descriptionBuilder.append(stringResource(Res.string.month_range_picker_content_description_month_in_range))
    }
    if (isCurrentYearMonth) {
        if (descriptionBuilder.isNotEmpty()) descriptionBuilder.append(", ")
        descriptionBuilder.append(stringResource(Res.string.month_range_picker_content_description_current_month))
    }
    return if (descriptionBuilder.isEmpty()) null else descriptionBuilder.toString()
}

@Composable
private fun MonthButton(
    yearMonth: YearMonth,
    modifier: Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    animateChecked: Boolean,
    enabled: Boolean,
    isCurrentYearMonth: Boolean,
    inRange: Boolean,
    description: String,
    colors: MonthRangePickerColors
) {
    val backgroundModifier =
        if (inRange && !selected) {
            Modifier
                .background(
                    color = colors.monthInRangeContainerColor,
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
                }.requiredSize(RecommendedSizeForAccessibility),
        enabled = enabled,
        shape = CircleShape,
        color =
            colors
                .monthContainerColor(
                    selected = selected,
                    enabled = enabled,
                    animate = animateChecked
                ).value,
        contentColor =
            colors
                .monthContentColor(
                    isCurrentMonth = isCurrentYearMonth,
                    selected = selected,
                    inRange = inRange,
                    enabled = enabled
                ).value,
        border =
            if (isCurrentYearMonth && !selected) {
                BorderStroke(
                    MonthRangePickerModalTokens.MonthTodayContainerOutlineWidth,
                    colors.currentMonthBorderColor
                )
            } else {
                null
            }
    ) {
        Box(
            modifier =
                Modifier.requiredSize(
                    MonthRangePickerModalTokens.MonthStateLayerWidth,
                    MonthRangePickerModalTokens.MonthStateLayerHeight
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(MonthNames[yearMonth.month - 1]),
                textAlign = TextAlign.Center,
                modifier = Modifier.clearAndSetSemantics {}
            )
        }
    }
}

internal object MonthRangePickerModalTokens {
    val MonthStateLayerHeight = 40.0.dp
    val MonthStateLayerWidth = 40.0.dp
    val MonthTodayContainerOutlineWidth = 1.0.dp
}

@Composable
private fun MonthEntryContainer(
    modifier: Modifier,
    title: (@Composable () -> Unit)?,
    headline: (@Composable () -> Unit)?,
    headlineTextStyle: TextStyle,
    headerMinHeight: Dp,
    colors: MonthRangePickerColors,
    content: @Composable () -> Unit
) {
    Column(
        modifier =
            modifier
                .defaultMinSize(minHeight = headerMinHeight)
                .background(colors.containerColor)
    ) {
        if (title != null) {
            Box(modifier = Modifier.padding(MonthRangePickerTitlePadding)) {
                title()
            }
        }

        if (headline != null) {
            ProvideTextStyle(value = headlineTextStyle) {
                Box(
                    modifier =
                        Modifier
                            .padding(MonthRangePickerHeadlinePadding)
                            .fillMaxWidth()
                ) {
                    headline()
                }
            }
        }

        content()
    }
}

object MonthRangePickerDefaults {
    val YearRange: IntRange = IntRange(2000, 2999)
    val MonthNames =
        listOf(
            Res.string.month_range_picker_january,
            Res.string.month_range_picker_february,
            Res.string.month_range_picker_march,
            Res.string.month_range_picker_april,
            Res.string.month_range_picker_may,
            Res.string.month_range_picker_june,
            Res.string.month_range_picker_july,
            Res.string.month_range_picker_august,
            Res.string.month_range_picker_september,
            Res.string.month_range_picker_october,
            Res.string.month_range_picker_november,
            Res.string.month_range_picker_december
        )

    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
        titleContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        headlineContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        monthContentColor: Color = MaterialTheme.colorScheme.onSurface,
        monthInRangeContentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        selectedMonthContentColor: Color = MaterialTheme.colorScheme.onPrimary,
        disabledSelectedMonthContentColor: Color = MaterialTheme.colorScheme.onPrimary.copy(alpha = DISABLED_ALPHA),
        monthContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        monthInRangeContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        disabledMonthContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = DISABLED_ALPHA),
        selectedMonthContainerColor: Color = MaterialTheme.colorScheme.primary,
        currentMonthContentColor: Color = MaterialTheme.colorScheme.primary,
        currentMonthBorderColor: Color = MaterialTheme.colorScheme.primary,
        disabledSelectedMonthContainerColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = DISABLED_ALPHA)
    ): MonthRangePickerColors =
        MonthRangePickerColors(
            containerColor = containerColor,
            titleContentColor = titleContentColor,
            headlineContentColor = headlineContentColor,
            monthContentColor = monthContentColor,
            monthInSelectionRangeContentColor = monthInRangeContentColor,
            selectedMonthContentColor = selectedMonthContentColor,
            monthContainerColor = monthContainerColor,
            monthInRangeContainerColor = monthInRangeContainerColor,
            selectedMonthContainerColor = selectedMonthContainerColor,
            currentMonthContentColor = currentMonthContentColor,
            disabledSelectedMonthContentColor = disabledSelectedMonthContentColor,
            disabledMonthContentColor = disabledMonthContentColor,
            currentMonthBorderColor = currentMonthBorderColor,
            disabledSelectedMonthContainerColor = disabledSelectedMonthContainerColor
        )

    @Composable
    fun MonthRangePickerTitle(modifier: Modifier = Modifier) {
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
        val headlineText =
            when {
                selectedStartYearMonth == null && selectedEndYearMonth == null -> "No selection"
                selectedStartYearMonth != null && selectedEndYearMonth == null ->
                    "Start: ${selectedStartYearMonth.month}/${selectedStartYearMonth.year}"

                else -> "Range: ${selectedStartYearMonth?.month}/${selectedStartYearMonth?.year} - " +
                        "${selectedEndYearMonth?.month}/${selectedEndYearMonth?.year}"
            }

        Text(
            text = headlineText,
            style = MaterialTheme.typography.bodyLarge,
            modifier =
                modifier.semantics {
                    liveRegion = LiveRegionMode.Polite
                }
        )
    }

    /** A default [SelectableMonths] that allows all dates to be selected. */
    val AllDates: SelectableMonths = object : SelectableMonths {}
}

class MonthRangePickerColors(
    val containerColor: Color,
    val titleContentColor: Color,
    val headlineContentColor: Color,
    val monthContentColor: Color,
    val monthInSelectionRangeContentColor: Color,
    val selectedMonthContentColor: Color,
    val monthContainerColor: Color,
    val monthInRangeContainerColor: Color,
    val selectedMonthContainerColor: Color,
    val currentMonthContentColor: Color,
    val currentMonthBorderColor: Color,
    val disabledSelectedMonthContentColor: Color,
    val disabledMonthContentColor: Color,
    val disabledSelectedMonthContainerColor: Color
) {
    @Composable
    internal fun monthContentColor(
        isCurrentMonth: Boolean,
        selected: Boolean,
        inRange: Boolean,
        enabled: Boolean
    ): State<Color> {
        val target =
            when {
                selected && enabled -> selectedMonthContentColor
                selected && !enabled -> disabledSelectedMonthContentColor
                inRange && enabled -> monthInSelectionRangeContentColor
                inRange && !enabled -> disabledMonthContentColor
                isCurrentMonth -> currentMonthContentColor
                enabled -> monthContentColor
                else -> disabledMonthContentColor
            }

        return if (inRange) {
            rememberUpdatedState(target)
        } else {
            // Animate the content color only when the day is not in a range.
            animateColorAsState(target, tween(durationMillis = DURATION_SHORT_2.toInt()))
        }
    }

    @Composable
    internal fun monthContainerColor(
        selected: Boolean,
        enabled: Boolean,
        animate: Boolean
    ): State<Color> {
        val target =
            if (selected) {
                if (enabled) selectedMonthContainerColor else disabledSelectedMonthContainerColor
            } else {
                Color.Transparent
            }
        return if (animate) {
            animateColorAsState(target, tween(durationMillis = DURATION_SHORT_2.toInt()))
        } else {
            rememberUpdatedState(target)
        }
    }
}

private val MonthRangePickerTitlePadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp)
private val MonthRangePickerHeadlinePadding =
    PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
internal val RecommendedSizeForAccessibility = 48.dp

const val DURATION_SHORT_2 = 100.0
internal const val DISABLED_ALPHA = 0.38f
private const val CHUNKED_MONTHS = 4