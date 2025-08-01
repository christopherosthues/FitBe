package org.darthacheron.fitbe.components.date

data class Year(val value: Int) : Comparable<Year> {
    init {
        require(value in 1..9999) { "Year must be between 1 and 9999" }
    }

    fun until(other: Year): Int = other.value - this.value

    override fun compareTo(other: Year): Int = value.compareTo(other.value)
    override fun toString(): String = value.toString()
}