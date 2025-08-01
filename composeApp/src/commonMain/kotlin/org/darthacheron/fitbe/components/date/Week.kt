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
import androidx.compose.foundation.lazy.items
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
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import org.darthacheron.fitbe.utils.isoWeekAndYear

@Composable
fun WeekRangePicker(
    startYear: Int = 2000,
    endYear: Int = 2999,
    onRangeSelected: (start: YearWeek, end: YearWeek) -> Unit
) {
    var selectedStart by remember { mutableStateOf<YearWeek?>(null) }
    var selectedEnd by remember { mutableStateOf<YearWeek?>(null) }

    val orderedStart = if (selectedStart != null && selectedEnd != null)
        minOf(selectedStart!!, selectedEnd!!) else null
    val orderedEnd = if (selectedStart != null && selectedEnd != null)
        maxOf(selectedStart!!, selectedEnd!!) else null

    val isComplete = selectedStart != null && selectedEnd != null
    val weekDiff = if (orderedStart != null && orderedEnd != null)
        orderedStart.weeksUntil(orderedEnd) else 0
    val isValid = isComplete && weekDiff <= 6

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        for (year in startYear..endYear) {
            val totalWeeks = getWeeksInYear(year)

            item {
                Text(
                    "$year",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(totalWeeks.chunked(4)) { weekRow ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    weekRow.forEach { weekNum ->
                        val yw = YearWeek(year, weekNum)
                        WeekButton(
                            yearWeek = yw,
                            isSelected = yw == selectedStart || yw == selectedEnd,
                            isInRange = orderedStart != null && orderedEnd != null && yw in orderedStart..orderedEnd,
                            onClick = {
                                if (selectedStart == null || (selectedStart != null && selectedEnd != null)) {
                                    selectedStart = yw
                                    selectedEnd = null
                                } else {
                                    selectedEnd = yw
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
                Text("Range must be 7 weeks or less", color = Color.Red)
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
fun WeekButton(
    yearWeek: YearWeek,
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

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(72.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text("W${yearWeek.week}", color = contentColor, style = MaterialTheme.typography.bodySmall)
    }
}

