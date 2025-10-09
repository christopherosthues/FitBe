package org.darthacheron.fitbe.components.validators

class KcalValidator {
    fun validate(value: UInt?): Boolean = value == null || value <= 10_000u
}