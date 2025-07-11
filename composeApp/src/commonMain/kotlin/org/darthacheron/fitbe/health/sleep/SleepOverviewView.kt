package org.darthacheron.fitbe.health.sleep


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Preview
@Composable
fun SleepOverviewView(viewModel: SleepViewModel) {
    val sleeps by viewModel.sleeps.collectAsState()
    val viewType by viewModel.viewType.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row {
            Button(onClick = { viewModel.setViewType(SleepViewType.WEEK) }) { Text("Week") }
            Button(onClick = { viewModel.setViewType(SleepViewType.MONTH) }) { Text("Month") }
            Button(onClick = { viewModel.setViewType(SleepViewType.YEAR) }) { Text("Year") }
        }
        Spacer(Modifier.height(8.dp))
        Row {
            Text("From: $startDate")
            Spacer(Modifier.width(8.dp))
            Text("To: $endDate")
        }
        Spacer(Modifier.height(8.dp))
        // Placeholder for chart
        Box(Modifier.height(200.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("Chart goes here")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { showAddDialog = true }) { Text("Add Sleep") }
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            items(sleeps) { sleep ->
                Text("Date: ${sleep.dateUtc}, Hours: ${sleep.hours}, Minutes: ${sleep.minutes}")
            }
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

@OptIn(ExperimentalTime::class)
@Composable
fun AddSleepDialog(onAdd: (Int, Int, LocalDate) -> Unit, onDismiss: () -> Unit) {
    var hours by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.UTC).date) }
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

