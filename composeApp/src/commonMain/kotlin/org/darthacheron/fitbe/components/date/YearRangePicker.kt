package org.darthacheron.fitbe.components.date

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun YearRangePicker(
    startFrom: Int = 1900,
    endAt: Int = 2100,
    onRangeSelected: (start: Year, end: Year) -> Unit
) {
    var selectedStart by remember { mutableStateOf<Year?>(null) }
    var selectedEnd by remember { mutableStateOf<Year?>(null) }

    val allYears = (startFrom..endAt).map(::Year)
    val chunkedYears = allYears.chunked(4)

    val isComplete = selectedStart != null && selectedEnd != null
    val orderedStart = if (selectedStart != null && selectedEnd != null)
        minOf(selectedStart!!, selectedEnd!!) else null
    val orderedEnd = if (selectedStart != null && selectedEnd != null)
        maxOf(selectedStart!!, selectedEnd!!) else null
    val rangeLength = if (orderedStart != null && orderedEnd != null)
        orderedStart.until(orderedEnd) else 0
    val isValid = isComplete && rangeLength <= 6

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Select a year range (max 7 years):", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(chunkedYears) { rowYears ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    rowYears.forEach { year ->
                        val isInRange = orderedStart != null && orderedEnd != null && year in orderedStart..orderedEnd
                        val isSelected = year == selectedStart || year == selectedEnd

                        YearButton(
                            year = year,
                            isInRange = isInRange,
                            isSelected = isSelected,
                            onClick = {
                                if (selectedStart == null || (selectedStart != null && selectedEnd != null)) {
                                    selectedStart = year
                                    selectedEnd = null
                                } else {
                                    selectedEnd = year
                                }
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (!isValid && isComplete) {
            Text(
                "Range must be 7 years or less.",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = { onRangeSelected(orderedStart!!, orderedEnd!!) },
            enabled = isValid
        ) {
            Text("Confirm Range")
        }
    }
}

@Composable
fun YearButton(
    year: Year,
    isInRange: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isInRange -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
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

        Text(
            text = year.toString(),
            // The semantics are set at the Day level.
            modifier = Modifier.clearAndSetSemantics {},
            textAlign = TextAlign.Center,
            color = contentColor)
    }
}
