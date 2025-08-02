package org.darthacheron.fitbe.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
object PastOrPresentSelectableDates : SelectableDates {
    private val timeZone = TimeZone.Companion.currentSystemDefault()
    private val today = Clock.System.now().toLocalDateTime(timeZone).date

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val date = Instant.Companion.fromEpochMilliseconds(utcTimeMillis)
            .toLocalDateTime(timeZone).date
        return date <= today
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= today.year
    }
}