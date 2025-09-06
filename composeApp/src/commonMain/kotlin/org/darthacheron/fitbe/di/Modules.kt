package org.darthacheron.fitbe.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.darthacheron.fitbe.StartUpService
import org.darthacheron.fitbe.database.DatabaseFactory
import org.darthacheron.fitbe.database.FitBeDatabase
import org.darthacheron.fitbe.database.PrepopulateCallback
import org.darthacheron.fitbe.exercises.equipment.EquipmentDao
import org.darthacheron.fitbe.exercises.equipment.TrainingEquipmentDetailViewModel
import org.darthacheron.fitbe.exercises.equipment.EquipmentRepository
import org.darthacheron.fitbe.exercises.exercises.ExerciseDao
import org.darthacheron.fitbe.exercises.exercises.ExercisesViewModel
import org.darthacheron.fitbe.exercises.equipment.TrainingEquipmentViewModel
import org.darthacheron.fitbe.exercises.exercises.ExerciseDetailViewModel
import org.darthacheron.fitbe.exercises.exercises.ExerciseRepository
import org.darthacheron.fitbe.health.HealthOverviewViewModel
import org.darthacheron.fitbe.health.beverages.BeverageOverviewViewModel
import org.darthacheron.fitbe.health.nutrition.NutritionOverviewViewModel
import org.darthacheron.fitbe.health.beverages.BeverageRepository
import org.darthacheron.fitbe.health.beverages.BeverageViewModel
import org.darthacheron.fitbe.health.sleep.SleepRepository
import org.darthacheron.fitbe.health.sleep.SleepViewModel
import org.darthacheron.fitbe.health.steps.StepsRepository
import org.darthacheron.fitbe.health.steps.StepsViewModel
import org.darthacheron.fitbe.health.weight.BodyWeightRepository
import org.darthacheron.fitbe.health.weight.WeightOverviewViewModel
import org.darthacheron.fitbe.home.HomeViewModel
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.profile.ProfileViewModel
import org.darthacheron.fitbe.settings.SettingsViewModel
import org.darthacheron.fitbe.settings.converters.BodyMeasurementUnitConverter
import org.darthacheron.fitbe.settings.converters.DistanceUnitConverter
import org.darthacheron.fitbe.settings.converters.WeightUnitConverter
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    singleOf(::BeverageRepository)
    singleOf(::SleepRepository)
    singleOf(::ProfileRepository)
    singleOf(::BodyWeightRepository)
    singleOf(::StepsRepository)
    singleOf(::EquipmentRepository)
    singleOf(::ExerciseRepository)

    singleOf(::BodyMeasurementUnitConverter)
    singleOf(::DistanceUnitConverter)
    singleOf(::WeightUnitConverter)

    single {
        get<DatabaseFactory>().create()
            .addCallback(PrepopulateCallback({ get<ExerciseDao>() }, { get<EquipmentDao>() }))
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<FitBeDatabase>().beverageDao }
    single { get<FitBeDatabase>().sleepDao }
    single { get<FitBeDatabase>().bodyWeightDao }
    single { get<FitBeDatabase>().profileDao }
    single { get<FitBeDatabase>().stepsDao }
    single { get<FitBeDatabase>().exerciseDao }
    single { get<FitBeDatabase>().equipmentDao }

    singleOf(::StartUpService)

    viewModelOf(::HomeViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::NutritionOverviewViewModel)
    viewModelOf(::BeverageViewModel)
    viewModelOf(::BeverageOverviewViewModel)
    viewModelOf(::SleepViewModel)
    viewModelOf(::HealthOverviewViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::WeightOverviewViewModel)
    viewModelOf(::StepsViewModel)
    viewModelOf(::ExercisesViewModel)
    viewModelOf(::ExerciseDetailViewModel)
    viewModelOf(::TrainingEquipmentViewModel)
    viewModelOf(::TrainingEquipmentDetailViewModel)
}
