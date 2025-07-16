package org.darthacheron.fitbe.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import org.darthacheron.fitbe.database.FitBeDatabase
import org.darthacheron.fitbe.database.seedDatabase
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.datetime.Clock
import kotlin.time.ExperimentalTime

@Serializable
object Home

@OptIn(ExperimentalTime::class)
@Composable
@Preview
fun HomeView(navController: NavController) {
//    val viewModel = koinViewModel<MyViewModel>()
//    val db = getKoin().get<FitBeDatabase>()
    Column() {
        Text(text = Clock.System.now().toString())
        Text(text = Clock.System.now().toLocalDateTime(TimeZone.UTC).toString())
        Text(text = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.toString())
        Text(text = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.minus(1, DateTimeUnit.WEEK).toString())
        Text(text = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.atStartOfDayIn(TimeZone.UTC).toString())
        Text(text = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.atTime(23, 59, 59, 0).toString())
        Text(text = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.atTime(23, 59, 59, 0).toInstant(
            TimeZone.UTC).toString())
        Text(text = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.minus(1, DateTimeUnit.WEEK).atStartOfDayIn(TimeZone.UTC).toString())
        Text(text = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.minus(1, DateTimeUnit.WEEK).atTime(23, 59, 59, 0).toString())
        Text(text = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.minus(1, DateTimeUnit.WEEK).atTime(23, 59, 59, 0).toInstant(
            TimeZone.UTC).toString())
    }
//    viewModel.viewModelScope.launch { seedDatabase(db) }
}



//✅ Visual Output
//
//This creates a filled inner surface with a wavy top, animated like fluid inside a circle.
//🔧 Parameters You Can Tune
//Parameter	Effect
//amplitude	Height of the wave peaks
//wavelength	Distance between wave crests
//step	Resolution of wave drawing (smaller = smoother)
//progress	How full the wave should rise
//
//Would you like to:
//
//Add a second wave layer with a different speed (to mimic water interference)?
//
//Apply a gradient to the wave fill?
//
//Let me know — we can keep evolving this!