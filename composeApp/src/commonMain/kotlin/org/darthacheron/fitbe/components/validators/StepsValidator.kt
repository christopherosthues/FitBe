package org.darthacheron.fitbe.components.validators

class StepsValidator {
    fun validate(value: UInt?): Boolean {
        return value == null || value < 500_000u
    }
}