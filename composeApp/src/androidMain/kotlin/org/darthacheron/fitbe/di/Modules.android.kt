package org.darthacheron.fitbe.di

import org.darthacheron.fitbe.database.DatabaseFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

actual val platformModule = module {
    single { DatabaseFactory(androidApplication()) }
}