package org.darthacheron.fitbe.health.beverages

import kotlinx.datetime.LocalDate

data class BeverageOverview(
    val dateUtc: LocalDate,
    val amount: UInt,
)
