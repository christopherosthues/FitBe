package org.darthacheron.fitbe.nutrition.beverages

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Beverage(
    val id: Uuid,
    val dateUtc: String,
    val dateLocal: String,
    val amount: Int,
    val beverage: String,
    val unit: FluidUnit
)
