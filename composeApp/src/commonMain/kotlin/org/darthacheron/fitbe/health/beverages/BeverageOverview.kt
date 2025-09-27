package org.darthacheron.fitbe.health.beverages

import kotlinx.datetime.LocalDate

data class BeverageOverview(
    val dateUtc: LocalDate,
    val amountMl: UInt,
)
