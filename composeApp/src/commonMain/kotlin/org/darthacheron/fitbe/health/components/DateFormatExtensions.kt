package org.darthacheron.fitbe.health.components

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

/**
 * Formats the [LocalDate] to a string based on the provided format pattern.
 *
 * This function uses the modern `kotlinx-datetime` library's formatting capabilities.
 * It allows for flexible formatting using a Unicode pattern string.
 *
 * Example usage:
 *
 * val date = LocalDate(2023, 1, 1)
 * val formatted = date.format("yyyy-MM-dd") // "2023-01-01"
 *
 * @param format A Unicode-based pattern string.
 *        Common pattern letters:
 *        - `y`: Year
 *        - `M`: Month of year
 *        - `d`: Day of month
 *        For a full list of pattern letters, refer to the `kotlinx-datetime` documentation.
 * @return The formatted date string.
 */
@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDate.format(format: String): String {
    val formatter = LocalDate.Format { byUnicodePattern(format) }
    return formatter.format(this)
}
