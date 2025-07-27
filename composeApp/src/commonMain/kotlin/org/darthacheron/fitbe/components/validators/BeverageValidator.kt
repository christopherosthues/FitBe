package org.darthacheron.fitbe.components.validators

class BeverageValidator {
    fun validate(value: UInt?): Boolean {
        return value != null && value > 5000u
    }
}