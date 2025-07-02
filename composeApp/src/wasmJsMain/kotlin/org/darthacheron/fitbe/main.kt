package org.darthacheron.fitbe

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.darthacheron.fitbe.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    ComposeViewport(document.body!!) {
        App()
    }
}