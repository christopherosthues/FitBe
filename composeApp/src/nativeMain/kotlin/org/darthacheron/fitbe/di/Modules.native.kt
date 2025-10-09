package org.darthacheron.fitbe.di

import org.darthacheron.fitbe.database.DatabaseFactory
import org.darthacheron.fitbe.settings.NativeSettingsRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.koin.dsl.module

actual val platformModule =
    module {
        single { DatabaseFactory() }
        single<SettingsRepository> { NativeSettingsRepository() }
    }