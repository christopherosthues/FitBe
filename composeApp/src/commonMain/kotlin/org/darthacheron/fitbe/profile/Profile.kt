package org.darthacheron.fitbe.profile

import kotlinx.datetime.LocalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Profile(
    val id: Uuid = Uuid.random(),
    val name: String = "Default",
    val gender: Gender = Gender.UNKNOWN,
    val targetKcal: UInt = 2000u,
    val targetBeverageInMilliliter: UInt = 2000u,
    val targetWeight: Double = 70.0,
    val targetSleepDuration: LocalTime = LocalTime(8, 0),
    val targetSteps: UInt = 10000u,
    var bodyHeight: Double = 170.0
)
