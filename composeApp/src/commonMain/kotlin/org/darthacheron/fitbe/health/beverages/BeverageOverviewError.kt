package org.darthacheron.fitbe.health.beverages

import org.darthacheron.fitbe.ui.UiStateError
import org.jetbrains.compose.resources.StringResource

class BeverageOverviewError(
    generalError: StringResource? = null,
) : UiStateError(generalError)