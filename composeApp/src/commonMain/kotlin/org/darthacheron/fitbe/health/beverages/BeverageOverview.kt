package org.darthacheron.fitbe.health.beverages

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.health.components.Overview

data class BeverageOverview(
    override val date: LocalDate,
    val amountMl: Double
) : Overview