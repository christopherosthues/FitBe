package org.darthacheron.fitbe.health.steps

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.health.componenets.Overview

data class StepsOverview(
    override val date: LocalDate,
    val steps: UInt,
) : Overview
