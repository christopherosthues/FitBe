package org.darthacheron.fitbe.settings.export

import kotlinx.serialization.Serializable
import org.darthacheron.fitbe.health.beverages.Beverage
import org.darthacheron.fitbe.health.sleep.Sleep
import org.darthacheron.fitbe.health.steps.Steps
import org.darthacheron.fitbe.health.weight.BodyWeight
import org.darthacheron.fitbe.profile.Profile

@Serializable
data class FitBeExportData(
    val profile: Profile,
    val beverages: List<Beverage> = emptyList(),
    val steps: List<Steps> = emptyList(),
    val sleep: List<Sleep> = emptyList(),
    val bodyWeights: List<BodyWeight> = emptyList(),
)
