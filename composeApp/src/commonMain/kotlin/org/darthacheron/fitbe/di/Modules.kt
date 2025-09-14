package org.darthacheron.fitbe.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.darthacheron.fitbe.StartUpService
import org.darthacheron.fitbe.database.DatabaseFactory
import org.darthacheron.fitbe.database.FitBeDatabase
import org.darthacheron.fitbe.database.PrepopulateCallback
import org.darthacheron.fitbe.workouts.ExercisesDashboardViewModel
import org.darthacheron.fitbe.workouts.equipment.EquipmentDao
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipmentDetailViewModel
import org.darthacheron.fitbe.workouts.equipment.EquipmentRepository
import org.darthacheron.fitbe.workouts.exercises.ExerciseDao
import org.darthacheron.fitbe.workouts.exercises.ExercisesViewModel
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipmentViewModel
import org.darthacheron.fitbe.workouts.exercises.ExerciseDetailViewModel
import org.darthacheron.fitbe.workouts.exercises.ExerciseRepository
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateDao
import org.darthacheron.fitbe.workouts.workouts.WorkoutExecutionRepository
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateRepository
import org.darthacheron.fitbe.workouts.workouts.PerformedWorkoutsOverviewViewModel
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplatesOverviewViewModel
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
import org.darthacheron.fitbe.ui.ActualTopBarManager
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateDetailViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    // Repositories
    singleOf(::ExerciseRepository)
    singleOf(::EquipmentRepository)
    singleOf(::StepsRepository)
    singleOf(::SleepRepository)
    singleOf(::BeverageRepository)
    singleOf(::BodyWeightRepository)
    singleOf(::ProfileRepository)
    singleOf(::WorkoutExecutionRepository)
    singleOf(::WorkoutTemplateRepository)

    // Unit Converters
    singleOf(::BodyMeasurementUnitConverter)
    singleOf(::DistanceUnitConverter)
    singleOf(::WeightUnitConverter)

    // Database and DAOs
    single {
        get<DatabaseFactory>().create()
            .addCallback(PrepopulateCallback({ get<ExerciseDao>() }, { get<EquipmentDao>() }, { get<WorkoutTemplateDao>() })) // Added WorkoutTemplateDao
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
    single { get<FitBeDatabase>().workoutTemplateDao }
    single { get<FitBeDatabase>().workoutExecutionSessionDao }

    // Services and Managers
    singleOf(::StartUpService)
    single<TopBarManager> { ActualTopBarManager() }

    // ViewModels
    viewModelOf(::HomeViewModel)
    viewModelOf(::ExercisesDashboardViewModel)
    viewModelOf(::ExercisesViewModel)
    viewModelOf(::ExerciseDetailViewModel)
    viewModelOf(::TrainingEquipmentViewModel)
    viewModelOf(::TrainingEquipmentDetailViewModel)
    viewModelOf(::HealthOverviewViewModel)
    viewModelOf(::StepsViewModel)
    viewModelOf(::SleepViewModel)
    viewModelOf(::BeverageOverviewViewModel)
    viewModelOf(::BeverageViewModel)
    viewModelOf(::WeightOverviewViewModel)
    viewModelOf(::NutritionOverviewViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::PerformedWorkoutsOverviewViewModel)
    viewModelOf(::WorkoutTemplatesOverviewViewModel)
    viewModelOf(::WorkoutTemplateDetailViewModel)
}

