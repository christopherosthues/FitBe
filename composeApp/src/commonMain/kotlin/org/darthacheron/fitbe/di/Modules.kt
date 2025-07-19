package org.darthacheron.fitbe.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.darthacheron.fitbe.database.DatabaseFactory
import org.darthacheron.fitbe.database.FitBeDatabase
import org.darthacheron.fitbe.exercises.ExercisesViewModel
import org.darthacheron.fitbe.health.HealthOverviewViewModel
import org.darthacheron.fitbe.health.nutrition.NutritionOverviewViewModel
import org.darthacheron.fitbe.health.beverages.BeverageRepository
import org.darthacheron.fitbe.health.beverages.BeverageViewModel
import org.darthacheron.fitbe.health.sleep.SleepRepository
import org.darthacheron.fitbe.health.sleep.SleepViewModel
import org.darthacheron.fitbe.profile.ProfileDao
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.profile.ProfileViewModel
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    singleOf(::BeverageRepository)
    singleOf(::SleepRepository)
    singleOf(::ProfileRepository)

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<FitBeDatabase>().beverageDao }
    single { get<FitBeDatabase>().sleepDao }
    single { get<FitBeDatabase>().bodyWeightDao }
    single { get<FitBeDatabase>().profileDao }

    viewModelOf(::SettingsViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::NutritionOverviewViewModel)
    viewModelOf(::BeverageViewModel)
    viewModelOf(::SleepViewModel)
    viewModelOf(::HealthOverviewViewModel)
    viewModelOf(::ExercisesViewModel)
    viewModelOf(::ProfileViewModel)
}
