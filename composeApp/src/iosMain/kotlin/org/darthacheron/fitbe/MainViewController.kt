package org.darthacheron.fitbe

import androidx.compose.ui.window.ComposeUIViewController
import org.darthacheron.fitbe.di.initKoin

fun MainViewController() =
    ComposeUIViewController(
        configure = {
            initKoin()
        }
    ) { App() }