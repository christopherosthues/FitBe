package org.darthacheron.fitbe.health.sleep

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Sleep(
    val id: Uuid,
    val hours: Int,
    val minutes: Int,
    val dateUtc: String,
    val dateLocal: String,
) {
}