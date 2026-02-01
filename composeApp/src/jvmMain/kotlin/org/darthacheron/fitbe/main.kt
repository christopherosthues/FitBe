package org.darthacheron.fitbe

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_launcher
import org.darthacheron.fitbe.di.initKoin
import org.jetbrains.compose.resources.painterResource

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "FitBe",
            icon = painterResource(Res.drawable.ic_launcher)
        ) {
            App()
        }
    }
}