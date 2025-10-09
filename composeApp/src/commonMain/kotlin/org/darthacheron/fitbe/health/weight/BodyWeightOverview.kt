package org.darthacheron.fitbe.health.weight

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.health.componenets.Overview

data class BodyWeightOverview(
    override val date: LocalDate,
    val weight: Double,
    val muscleMass: Double,
    val boneMass: Double,
    val bodyFatPercentage: Double,
    val bodyWaterPercentage: Double
) : Overview