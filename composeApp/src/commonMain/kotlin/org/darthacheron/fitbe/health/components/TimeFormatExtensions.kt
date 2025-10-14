package org.darthacheron.fitbe.health.components

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

/**
 * Formats the [LocalTime] to a string based on the provided format pattern.
 *
 * This function uses the modern `kotlinx-datetime` library's formatting capabilities.
 * It allows for flexible formatting using a Unicode pattern string.
 *
 * Example usage:
 *
 * val time = LocalTime(14, 30, 15)
 * val formatted = time.format("HH:mm:ss") // "14:30:15"
 * val formattedAmPm = time.format("h:mm a") // "2:30 PM"
 *
 * @param format A Unicode-based pattern string.
 *        Common pattern letters:
 *        - `H`: Hour of day (0-23)
 *        - `h`: Hour in am/pm (1-12)
 *        - `m`: Minute in hour
 *        - `s`: Second in minute
 *        - `a`: Am/pm marker
 *        For a full list of pattern letters, refer to the `kotlinx-datetime` documentation.
 * @return The formatted time string.
 */
@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalTime.format(format: String): String {
    val formatter = LocalTime.Format { byUnicodePattern(format) }
    return formatter.format(this)
}