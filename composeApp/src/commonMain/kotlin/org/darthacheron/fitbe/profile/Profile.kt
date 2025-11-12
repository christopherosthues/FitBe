package org.darthacheron.fitbe.profile

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class Profile(
    val id: Uuid = Uuid.random(),
    val name: String = ProfileDefaults.NAME,
    val gender: Gender = ProfileDefaults.gender,
    val targetKcal: UInt? = ProfileDefaults.KCAL,
    val targetBeverageInMilliliter: UInt? = ProfileDefaults.BEVERAGE,
    val targetWeight: Double? = ProfileDefaults.WEIGHT_IN_KG,
    val targetSleepDuration: UInt? = ProfileDefaults.SLEEP_DURATION,
    val targetSteps: UInt? = ProfileDefaults.STEPS,
    var bodyHeight: Double? = ProfileDefaults.BODY_HEIGHT_IN_CM,
    val dateOfBirth: LocalDate? =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.UTC)
            .date
)