package org.darthacheron.fitbe.components.validators

class PositiveNumberValidator {
    fun validate(value: String): Boolean {
        val regex = Regex("^\\d*$")
        return regex.matches(value)
    }
}