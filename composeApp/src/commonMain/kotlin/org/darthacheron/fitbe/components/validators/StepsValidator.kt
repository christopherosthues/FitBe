package org.darthacheron.fitbe.components.validators

class StepsValidator {
    fun validate(value: UInt?): Boolean = value == null || value < 500_000u
}