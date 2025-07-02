package org.darthacheron.fitbe

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.darthacheron.fitbe.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "FitBe",
        ) {
            App()
        }
    }
}