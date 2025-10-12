package org.darthacheron.fitbe.health.sleep

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.health.components.Overview

data class SleepOverview(
    override val date: LocalDate,
    val totalMinutes: Double
) : Overview