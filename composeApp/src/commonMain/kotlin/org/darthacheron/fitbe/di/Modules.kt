package org.darthacheron.fitbe.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.darthacheron.fitbe.StartUpService
import org.darthacheron.fitbe.components.validators.BeverageValidator
import org.darthacheron.fitbe.components.validators.BodyHeightValidator
import org.darthacheron.fitbe.components.validators.BodyWeightValidator
import org.darthacheron.fitbe.components.validators.KcalValidator
import org.darthacheron.fitbe.components.validators.PercentageValidator
import org.darthacheron.fitbe.components.validators.PositiveDecimalValidator
import org.darthacheron.fitbe.components.validators.PositiveNumberValidator
import org.darthacheron.fitbe.components.validators.StepsValidator
import org.darthacheron.fitbe.database.DatabaseFactory
import org.darthacheron.fitbe.database.FitBeDatabase
import org.darthacheron.fitbe.database.PrepopulateCallback
import org.darthacheron.fitbe.health.HealthOverviewViewModel
import org.darthacheron.fitbe.health.beverages.BeverageDailyViewModel
import org.darthacheron.fitbe.health.beverages.BeverageOverviewViewModel
import org.darthacheron.fitbe.health.beverages.BeverageRepository
import org.darthacheron.fitbe.health.beverages.manage.AddBeverageDialogViewModel
import org.darthacheron.fitbe.health.beverages.manage.EditBeverageDialogViewModel
import org.darthacheron.fitbe.health.components.HealthViewModel
import org.darthacheron.fitbe.health.nutrition.NutritionOverviewViewModel
import org.darthacheron.fitbe.health.sleep.SleepDailyViewModel
import org.darthacheron.fitbe.health.sleep.SleepOverviewViewModel
import org.darthacheron.fitbe.health.sleep.SleepRepository
import org.darthacheron.fitbe.health.sleep.manage.AddSleepDialogViewModel
import org.darthacheron.fitbe.health.sleep.manage.EditSleepDialogViewModel
import org.darthacheron.fitbe.health.steps.StepsDailyViewModel
import org.darthacheron.fitbe.health.steps.StepsOverviewViewModel
import org.darthacheron.fitbe.health.steps.StepsRepository
import org.darthacheron.fitbe.health.steps.manage.AddStepsDialogViewModel
import org.darthacheron.fitbe.health.steps.manage.EditStepsDialogViewModel
import org.darthacheron.fitbe.health.weight.BodyWeightDailyViewModel
import org.darthacheron.fitbe.health.weight.BodyWeightOverviewViewModel
import org.darthacheron.fitbe.health.weight.BodyWeightRepository
import org.darthacheron.fitbe.health.weight.manage.AddBodyWeightDialogViewModel
import org.darthacheron.fitbe.health.weight.manage.EditBodyWeightDialogViewModel
import org.darthacheron.fitbe.home.HomeViewModel
import org.darthacheron.fitbe.home.summary.BeveragesSummaryViewModel
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.profile.ProfileViewModel
import org.darthacheron.fitbe.settings.SettingsViewModel
import org.darthacheron.fitbe.settings.converters.BodyMeasurementUnitConverter
import org.darthacheron.fitbe.settings.converters.DistanceUnitConverter
import org.darthacheron.fitbe.settings.converters.WeightUnitConverter
import org.darthacheron.fitbe.ui.ActualTopBarManager
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.workouts.ExercisesDashboardViewModel
import org.darthacheron.fitbe.workouts.equipment.EquipmentDao
import org.darthacheron.fitbe.workouts.equipment.EquipmentRepository
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipmentDetailViewModel
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipmentViewModel
import org.darthacheron.fitbe.workouts.exercises.ExerciseDao
import org.darthacheron.fitbe.workouts.exercises.ExerciseDetailViewModel
import org.darthacheron.fitbe.workouts.exercises.ExerciseRepository
import org.darthacheron.fitbe.workouts.exercises.ExercisesViewModel
import org.darthacheron.fitbe.workouts.programs.ProgramOverviewViewModel
import org.darthacheron.fitbe.workouts.programs.ProgramRepository
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateDao
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateDetailViewModel
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateRepository
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplatesOverviewViewModel
import org.darthacheron.fitbe.workouts.workouts.ExerciseExecutionViewModel
import org.darthacheron.fitbe.workouts.workouts.WorkoutExecutionRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule =
    module {
        // Repositories
        factoryOf(::ExerciseRepository)
        factoryOf(::EquipmentRepository)
        factoryOf(::StepsRepository)
        factoryOf(::SleepRepository)
        factoryOf(::BeverageRepository)
        factoryOf(::BodyWeightRepository)
        factoryOf(::ProfileRepository)
        factoryOf(::WorkoutTemplateRepository)
        factoryOf(::WorkoutExecutionRepository)
        factoryOf(::ProgramRepository)

        // Unit Converters
        factoryOf(::BodyMeasurementUnitConverter)
        factoryOf(::DistanceUnitConverter)
        factoryOf(::WeightUnitConverter)

        // Validators
        factoryOf(::BeverageValidator)
        factoryOf(::BodyHeightValidator)
        factoryOf(::BodyWeightValidator)
        factoryOf(::KcalValidator)
        factoryOf(::PositiveDecimalValidator)
        factoryOf(::PositiveNumberValidator)
        factoryOf(::StepsValidator)
        factoryOf(::PercentageValidator)

        single {
            get<DatabaseFactory>()
                .create()
                .addCallback(
                    PrepopulateCallback(
                        { get<ExerciseDao>() },
                        { get<EquipmentDao>() },
                        { get<WorkoutTemplateDao>() }
                    )
                ).setDriver(BundledSQLiteDriver())
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
        single { get<FitBeDatabase>().workoutExecutionDao }
        single { get<FitBeDatabase>().programDao }

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
        viewModelOf(::StepsOverviewViewModel)
        viewModelOf(::SleepOverviewViewModel)
        viewModelOf(::BeverageOverviewViewModel)
        viewModelOf(::BeverageDailyViewModel)
        viewModelOf(::BodyWeightOverviewViewModel)
        viewModelOf(::NutritionOverviewViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::SettingsViewModel)
        viewModelOf(::WorkoutTemplatesOverviewViewModel)
        viewModelOf(::WorkoutTemplateDetailViewModel)
        viewModelOf(::ExerciseExecutionViewModel)
        viewModelOf(::ProgramOverviewViewModel)
        viewModelOf(::AddBeverageDialogViewModel)
        viewModelOf(::EditBeverageDialogViewModel)
        viewModelOf(::AddStepsDialogViewModel)
        viewModelOf(::EditStepsDialogViewModel)
        viewModelOf(::AddBodyWeightDialogViewModel)
        viewModelOf(::EditBodyWeightDialogViewModel)
        viewModelOf(::AddSleepDialogViewModel)
        viewModelOf(::EditSleepDialogViewModel)
        viewModelOf(::HealthViewModel)
        viewModelOf(::SleepDailyViewModel)
        viewModelOf(::StepsDailyViewModel)
        viewModelOf(::BodyWeightDailyViewModel)
        viewModelOf(::BeveragesSummaryViewModel)
    }