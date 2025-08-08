package org.darthacheron.fitbe.components.date

import androidx.compose.runtime.Composable
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.sleep_view_type_day
import fitbe.composeapp.generated.resources.sleep_view_type_month
import fitbe.composeapp.generated.resources.sleep_view_type_week
import fitbe.composeapp.generated.resources.sleep_view_type_year
import org.jetbrains.compose.resources.stringResource

enum class DateUnit {
    DAY, WEEK, MONTH, YEAR;

    @Composable
    fun localizedString(): String {
        return when(this) {
            DAY -> stringResource(Res.string.sleep_view_type_day)
            WEEK -> stringResource(Res.string.sleep_view_type_week)
            MONTH -> stringResource(Res.string.sleep_view_type_month)
            YEAR -> stringResource(Res.string.sleep_view_type_year)
        }
    }
}