package org.darthacheron.fitbe.di

import org.darthacheron.fitbe.database.DatabaseFactory
import org.darthacheron.fitbe.dependencies.DbClient
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::DbClient)
    single { DatabaseFactory(androidApplication()) }
}