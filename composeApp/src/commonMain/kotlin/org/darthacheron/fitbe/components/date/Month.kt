package org.darthacheron.fitbe.components.date

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp

data class YearMonth(val year: Int, val month: Int) : Comparable<YearMonth> {
    init {
        require(month in 1..12) { "Month must be between 1 and 12" }
        require(year in 1..9999) { "Year must be between 1 and 9999" }
    }

    fun monthsUntil(other: YearMonth): Int {
        return (other.year - year) * 12 + (other.month - month)
    }

    override fun compareTo(other: YearMonth): Int {
        return when {
            year != other.year -> year.compareTo(other.year)
            else -> month.compareTo(other.month)
        }
    }

    override fun toString(): String {
        val monthStr = month.toString().padStart(2, '0')
        return "$year-$monthStr"
    }
}

@Composable
fun MonthRangePicker(
    startYear: Int = 2000,
    endYear: Int = 2999,
    onRangeSelected: (start: YearMonth, end: YearMonth) -> Unit
) {
    var selectedStart by remember { mutableStateOf<YearMonth?>(null) }
    var selectedEnd by remember { mutableStateOf<YearMonth?>(null) }

    val orderedStart = if (selectedStart != null && selectedEnd != null)
        minOf(selectedStart!!, selectedEnd!!) else null
    val orderedEnd = if (selectedStart != null && selectedEnd != null)
        maxOf(selectedStart!!, selectedEnd!!) else null

    val isComplete = selectedStart != null && selectedEnd != null
    val monthDiff = if (orderedStart != null && orderedEnd != null)
        orderedStart.monthsUntil(orderedEnd) else 0
    val isValid = isComplete && monthDiff <= 6

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        for (year in startYear..endYear) {
            item {
                Text(
                    "$year",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    (1..4).forEach { month ->
                        val ym = YearMonth(year, month)
                        MonthButton(
                            yearMonth = ym,
                            isSelected = ym == selectedStart || ym == selectedEnd,
                            isInRange = orderedStart != null && orderedEnd != null && ym in orderedStart..orderedEnd,
                            onClick = {
                                if (selectedStart == null || (selectedStart != null && selectedEnd != null)) {
                                    selectedStart = ym
                                    selectedEnd = null
                                } else {
                                    selectedEnd = ym
                                }
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    (5..8).forEach { month ->
                        val ym = YearMonth(year, month)
                        MonthButton(
                            yearMonth = ym,
                            isSelected = ym == selectedStart || ym == selectedEnd,
                            isInRange = orderedStart != null && orderedEnd != null && ym in orderedStart..orderedEnd,
                            onClick = {
                                if (selectedStart == null || (selectedStart != null && selectedEnd != null)) {
                                    selectedStart = ym
                                    selectedEnd = null
                                } else {
                                    selectedEnd = ym
                                }
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    (9..12).forEach { month ->
                        val ym = YearMonth(year, month)
                        MonthButton(
                            yearMonth = ym,
                            isSelected = ym == selectedStart || ym == selectedEnd,
                            isInRange = orderedStart != null && orderedEnd != null && ym in orderedStart..orderedEnd,
                            onClick = {
                                if (selectedStart == null || (selectedStart != null && selectedEnd != null)) {
                                    selectedStart = ym
                                    selectedEnd = null
                                } else {
                                    selectedEnd = ym
                                }
                            }
                        )
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
            if (!isValid && isComplete) {
                Text("Range must be 7 months or less", color = Color.Red)
            }

            Button(
                onClick = {
                    if (orderedStart != null && orderedEnd != null) {
                        onRangeSelected(orderedStart, orderedEnd)
                    }
                },
                enabled = isValid
            ) {
                Text("Confirm Range")
            }
        }
    }
}

@Composable
fun MonthButton(
    yearMonth: YearMonth,
    isSelected: Boolean,
    isInRange: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isInRange -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    val monthName = MonthNames[yearMonth.month - 1]

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(72.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(monthName, color = contentColor, style = MaterialTheme.typography.bodySmall)
    }
}
val MonthNames = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
)
