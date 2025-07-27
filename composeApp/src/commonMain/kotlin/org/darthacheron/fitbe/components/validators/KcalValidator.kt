package org.darthacheron.fitbe.components.validators

class KcalValidator {
    fun validate(value: UInt?): Boolean {
        return value != null && value > 10_000u
    }
}