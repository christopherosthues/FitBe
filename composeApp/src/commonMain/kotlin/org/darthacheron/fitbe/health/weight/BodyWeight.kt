package org.darthacheron.fitbe.health.weight

import org.darthacheron.fitbe.settings.WeightUnit
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class BodyWeight(
    val id: Uuid,
    val weight: Double,
    val dateUtc: String,
    val dateLocal: String,
    val unit: WeightUnit
)