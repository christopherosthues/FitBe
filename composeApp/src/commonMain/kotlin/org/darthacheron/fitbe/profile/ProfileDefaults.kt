package org.darthacheron.fitbe.profile

import kotlinx.datetime.LocalTime

object ProfileDefaults {
    const val NAME = "Default"
    val gender = Gender.UNKNOWN
    const val KCAL = 2000u
    const val BEVERAGE = 2000u
    const val WEIGHT_IN_KG = 70.0
    val SLEEP_DURATION = LocalTime(8, 0)
    const val STEPS = 10000u
    const val BODY_HEIGHT_IN_CM = 170.0
}