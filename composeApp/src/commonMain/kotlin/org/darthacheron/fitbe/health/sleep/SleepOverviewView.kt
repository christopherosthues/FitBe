package org.darthacheron.fitbe.health.sleep

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.gestures.GestureConfig
import io.github.koalaplot.core.line.StairstepPlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.DoubleLinearAxisModel
import io.github.koalaplot.core.xygraph.Point
import io.github.koalaplot.core.xygraph.XYGraph
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.ceil
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun SleepOverviewView(viewModel: SleepViewModel) {
    val sleeps by viewModel.sleeps.collectAsState()
    val viewType by viewModel.viewType.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()
//        .verticalScroll(scrollState)
        .padding(16.dp)) {
        Row {
            Button(onClick = { viewModel.setViewType(SleepViewType.WEEK) }) { Text("Week") }
            Button(onClick = { viewModel.setViewType(SleepViewType.MONTH) }) { Text("Month") }
            Button(onClick = { viewModel.setViewType(SleepViewType.YEAR) }) { Text("Year") }
        }
        Button(onClick = { showAddDialog = true }) { Text("Add Sleep") }
        Spacer(Modifier.height(8.dp))
        Row {
            Text("From: $startDate")
            Spacer(Modifier.width(8.dp))
            Text("To: $endDate")
        }
        Spacer(Modifier.height(8.dp))
        if (!sleeps.isEmpty()) {
            Plot(sleeps)
        }
    }
    if (showAddDialog) {
        AddSleepDialog(
            onAdd = { h, m, d ->
                viewModel.addSleep(h, m, d)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun Plot(sleeps: List<Point<LocalDate, Double>>) {
    ChartLayout(

    ) {
        val dates = sleeps.map { it.x }
        XYGraph(
            xAxisModel = CategoryAxisModel(dates),
            yAxisModel = DoubleLinearAxisModel(range = 0.0..sleeps.maxOf { ceil(it.y) }, minorTickCount = 1),
            xAxisLabels = {
                Text(
                    it.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 2.dp).graphicsLayer {
                        rotationZ = -75f
                    },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            },
            xAxisTitle = { null },
            gestureConfig = GestureConfig(zoomXEnabled = true, zoomYEnabled = true),
            yAxisLabels = {
                Text(
                    "${it.toInt()}h",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.absolutePadding(right = 2.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            },
        ) {
            StairstepPlot(
                data = sleeps,
                lineStyle = LineStyle(
                    brush = SolidColor(Color.Black),
                    strokeWidth = 2.dp
                )
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun AddSleepDialog(onAdd: (Int, Int, Instant) -> Unit, onDismiss: () -> Unit) {
    var hours by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("") }
    var date by remember {
        mutableStateOf(
            Clock.System.now().toLocalDateTime(TimeZone.UTC).date.atStartOfDayIn(
                TimeZone.UTC
            )
        )
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Sleep") },
        text = {
            Column {
                OutlinedTextField(value = hours, onValueChange = { hours = it }, label = { Text("Hours") })
                OutlinedTextField(value = minutes, onValueChange = { minutes = it }, label = { Text("Minutes") })
                // Date picker placeholder
                Text("Date: $date")
            }
        },
        confirmButton = {
            Button(onClick = {
                onAdd(hours.toIntOrNull() ?: 0, minutes.toIntOrNull() ?: 0, date)
            }) { Text("Add") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )
}

