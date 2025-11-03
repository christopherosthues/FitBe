package org.darthacheron.fitbe.components.date

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
object PastOrPresentSelectableDates : SelectableDates {
    private val timeZone = TimeZone.currentSystemDefault()
    private val today =
        Clock.System
            .now()
            .toLocalDateTime(timeZone)
            .date

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val date =
            Instant
                .fromEpochMilliseconds(utcTimeMillis)
                .toLocalDateTime(timeZone)
                .date
        return date <= today
    }

    override fun isSelectableYear(year: Int): Boolean = year <= today.year
}