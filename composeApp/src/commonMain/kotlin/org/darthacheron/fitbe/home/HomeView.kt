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
fun HomeView(homeViewModel: HomeViewModel, navController: NavController,) {
    Column() {

    }
}
