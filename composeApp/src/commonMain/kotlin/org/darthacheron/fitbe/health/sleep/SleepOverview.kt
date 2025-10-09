package org.darthacheron.fitbe.health.sleep

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.health.componenets.Overview

data class SleepOverview(
    override val date: LocalDate,
    val totalMinutes: Double
) : Overview {
    val minutes = totalMinutes % 60
    val hours = totalMinutes / 60
}