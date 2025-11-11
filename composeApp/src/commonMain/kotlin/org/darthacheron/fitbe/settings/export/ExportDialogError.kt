package org.darthacheron.fitbe.settings.export

import org.darthacheron.fitbe.ui.UiStateError
import org.jetbrains.compose.resources.StringResource

class ExportDialogError(generalError: StringResource? = null) : UiStateError(generalError = generalError)