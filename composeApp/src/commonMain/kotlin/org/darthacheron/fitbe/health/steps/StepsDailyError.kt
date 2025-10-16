package org.darthacheron.fitbe.health.steps

import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.ui.UiStateError
import org.jetbrains.compose.resources.StringResource

class StepsDailyError(
    generalError: StringResource? = null
) : UiStateError(generalError)