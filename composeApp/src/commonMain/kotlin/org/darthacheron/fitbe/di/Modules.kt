package org.darthacheron.fitbe.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.darthacheron.fitbe.database.DatabaseFactory
import org.darthacheron.fitbe.database.FitBeDatabase
import org.darthacheron.fitbe.dependencies.MyRepository
import org.darthacheron.fitbe.dependencies.MyRepositoryImp
import org.darthacheron.fitbe.dependencies.MyViewModel
import org.darthacheron.fitbe.nutrition.NutritionOverviewViewModel
import org.darthacheron.fitbe.nutrition.water.WaterIntakeRepository
import org.darthacheron.fitbe.nutrition.water.WaterIntakeViewModel
import org.darthacheron.fitbe.profile.ProfileViewModel
import org.darthacheron.fitbe.settings.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    singleOf(::MyRepositoryImp).bind<MyRepository>()
    singleOf(::WaterIntakeRepository)

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<FitBeDatabase>().waterConsumptionDao }

    viewModelOf(::MyViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::NutritionOverviewViewModel)
    viewModelOf(::WaterIntakeViewModel)
}