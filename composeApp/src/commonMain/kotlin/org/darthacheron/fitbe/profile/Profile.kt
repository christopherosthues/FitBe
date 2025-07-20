package org.darthacheron.fitbe.profile

import kotlinx.datetime.LocalTime
import kotlin.time.Duration
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Profile(
    val id: Uuid = Uuid.random(),
    val name: String,
    val gender: Gender,
    val targetKcal: UInt,
    val targetBeverageInMilliliter: UInt,
    val targetWeight: Double,
    val targetSleepDuration: LocalTime,
    val targetSteps: UInt
)