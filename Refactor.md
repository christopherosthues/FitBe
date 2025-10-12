# org
## darthacheron
### fitbe

* [x] App
* [x] RootScreen
* [x] StartUpService

#### components

* [x] CircularWaveAnimationProgressIndicator
* [ ] DropdownSelection
* [x] FitBeImage
* [x] HalfCircularProgressIndicator
* [x] ImagePlaceholder
* [x] ImageWithDefault
* [x] SaveCancelFloatingActionButtonRow

##### date

* [x] AdvancedTimerDialog
* [x] DatePickerModal
* [x] DateRange
* [ ] DateRangePicker
* [x] DateUnit
* [x] PastOrPresentSelectableDates
* [x] TimeInputDialog
* [x] TimerPickerDialog

###### month

* [ ] MonthRangePicker
* [x] PastOrPresentSelectableMonths

###### week

* [x] PastOrPresentSelectableWeeks
* [ ] WeekRangePicker

###### year

* [x] PastOrPresentSelectableYears
* [ ] YearRangePicker

##### validators

* [x] BeverageValidator
* [x] BodyHeightValidator
* [x] BodyWeightValidator
* [x] KcalValidator
* [x] PercentageValidator
* [x] PositiveDecimalValidator
* [x] PositiveNumberValidator
* [x] StepsValidator

#### database

* [x] DatabaseFactory
* [x] FitBeDatabase
* [x] FitBeDatabaseConstructor
* [ ] InitialData
* [x] PrepopulateCallback

##### converters

* [x] ExerciseTypeConverter
* [x] FluidUnitConverter
* [x] GenderConverter
* [x] InstantConverter
* [x] LocalDateConverter
* [x] LocalDateTimeConverter
* [x] LocalTimeConverter
* [x] MuscleGroupListConverter
* [x] RecommendedForListConverter
* [x] UuidConverter
* [x] WorkoutExecutionStatusConverter
* [x] WorkoutSetStatusConverter

#### di

* [x] Modules
* [x] initKoin

#### health

* [ ] HealthOverviewView
* [ ] HealthOverviewViewModel
* [x] ViewState

##### beverages

* [ ] AddBeverageDialog
* [ ] AddBeverageDialogUiState
* [ ] AddBeverageDialogViewModel
* [ ] Beverage
* [ ] BeverageDailyError
* [ ] BeverageDailyUiState
* [ ] BeverageDailyView
* [ ] BeverageDailyViewModel
* [ ] BeverageDao
* [ ] BeverageEntity
* [ ] BeverageOverview
* [ ] BeverageOverviewError
* [ ] BeverageOverviewUiState
* [x] BeverageOverviewView
* [x] BeverageOverviewViewModel
* [ ] BeverageRepository
* [ ] BeverageView
* [ ] FluidUnit
* [ ] PlotBeverages

##### componenets

* [ ] AddDialogViewModel
* [ ] DailyView
* [ ] DailyViewModel
* [ ] DateControl
* [ ] DateRangeControl
* [ ] DialogUiState
* [x] HealthView
* [x] HealthViewModel
* [ ] LocalDateExtensions
* [ ] Overview
* [ ] OverviewView
* [ ] OverviewViewModel

##### nutrition

* [ ] NutritionOverviewView
* [ ] NutritionOverviewViewModel

##### sleep

* [ ] AddSleepDialog
* [ ] AddSleepDialogUiState
* [ ] AddSleepDialogViewModel
* [ ] PlotSleeps
* [ ] Sleep
* [ ] SleepDailyView
* [ ] SleepDailyViewModel
* [ ] SleepDao
* [ ] SleepEntity
* [ ] SleepOverview
* [ ] SleepOverviewError
* [ ] SleepOverviewUiState
* [ ] SleepOverviewView
* [ ] SleepOverviewViewModel
* [ ] SleepRepository
* [ ] SleepView

##### steps

* [ ] AddStepsDialog
* [ ] AddStepsDialogUiState
* [ ] AddStepsDialogViewModel
* [ ] PlotSteps
* [ ] Steps
* [ ] StepsDailyView
* [ ] StepsDailyViewModel
* [ ] StepsDao
* [ ] StepsEntity
* [ ] StepsOverview
* [ ] StepsOverviewError
* [ ] StepsOverviewUiState
* [ ] StepsOverviewView
* [ ] StepsOverviewViewModel
* [ ] StepsRepository
* [ ] StepsView

##### weight

* [ ] AddBodyWeightDialog
* [ ] AddBodyWeightDialogUiState
* [ ] AddBodyWeightDialogViewModel
* [ ] BodyWeight
* [ ] BodyWeightDailyView
* [ ] BodyWeightDailyViewModel
* [ ] BodyWeightDao
* [ ] BodyWeightEntity
* [ ] BodyWeightOverview
* [ ] BodyWeightRepository
* [ ] BodyWeightView
* [ ] PlotBodyWeights
* [ ] WeightOverviewError
* [ ] WeightOverviewUiState
* [ ] WeightOverviewView
* [ ] WeightOverviewViewModel

#### home

* [ ] HomeView
* [ ] HomeViewModel

#### navigation

* [x] BottomBarDestination
* [ ] BottomBarNavGraph
* [ ] RootNavGraph
* [x] Screen

#### profile

* [ ] Gender
* [ ] Profile
* [ ] ProfileDao
* [ ] ProfileDefaults
* [ ] ProfileEntity
* [ ] ProfileError
* [ ] ProfileRepository
* [ ] ProfileSelectionDialog
* [ ] ProfileUiState
* [ ] ProfileView
* [ ] ProfileViewModel

#### settings

* [ ] BodyMeasurementUnit
* [ ] DistanceUnit
* [ ] Settings
* [ ] SettingsError
* [ ] SettingsKeys
* [ ] SettingsRepository
* [ ] SettingsUiState
* [ ] SettingsView
* [ ] SettingsViewModel
* [ ] ThemeMode
* [ ] WeightUnit

##### converters

* [ ] BodyMeasurementUnitConverter
* [ ] DistanceUnitConverter
* [ ] FluidUnitConverter
* [ ] WeightUnitConverter

#### ui

* [x] ActualTopBarManager
* [x] AppTheme
* [x] BottomNavigationBarViewModel
* [x] FilterableViewModel
* [x] FitBeViewModel
* [x] TopBarManager
* [x] UiState
* [x] UiStateError

##### state

* [x] TopBarAction
* [x] TopBarConfig

#### utils

* [x] DateUtils
* [x] DoubleUtils
* [x] MathUtils
* [x] StackedAreaPlotDoubleDataAdapter

#### workouts

* [ ] ExercisesDashboardView
* [ ] ExercisesDashboardViewModel
* [ ] TrainingGoal

##### equipment

* [ ] AddEditTrainingEquipmentUiState
* [ ] DefaultEquipmentResProvider
* [ ] DefaultTrainingEquipmentEntity
* [ ] EquipmentDao
* [ ] EquipmentRepository
* [ ] EquipmentWithExercises
* [ ] EquipmentWithExercisesEntity
* [ ] ProfileFavoriteEquipmentCrossRef
* [ ] TrainingEquipment
* [ ] TrainingEquipmentDetailView
* [ ] TrainingEquipmentDetailViewModel
* [ ] TrainingEquipmentEntity
* [ ] TrainingEquipmentError
* [ ] TrainingEquipmentScreenUiState
* [ ] TrainingEquipmentView
* [ ] TrainingEquipmentViewModel

##### exercises

* [ ] DefaultExerciseEntity
* [ ] DefaultExerciseEquipmentCrossRef
* [ ] DefaultExerciseResProvider
* [ ] DefaultExerciseWithEquipmentEntity
* [ ] Exercise
* [ ] ExerciseDao
* [ ] ExerciseDetailUiState
* [ ] ExerciseDetailView
* [ ] ExerciseDetailViewModel
* [ ] ExerciseEntity
* [ ] ExerciseEquipmentCrossRef
* [ ] ExerciseError
* [ ] ExerciseRepository
* [ ] ExerciseType
* [ ] ExerciseWithEquipment
* [ ] ExerciseWithEquipmentEntity
* [ ] ExercisesScreenUiState
* [ ] ExercisesView
* [ ] ExercisesViewModel
* [ ] MuscleGroup
* [ ] ProfileFavoriteExerciseCrossRef
* [ ] RecommendedFor

##### programs

* [ ] Program
* [ ] ProgramDao
* [ ] ProgramOverviewView
* [ ] ProgramOverviewViewModel
* [ ] ProgramRepository
* [ ] ProgramsUiState

##### templates

* [ ] DefaultWorkoutResProvider
* [ ] DefaultWorkoutTemplateEntity
* [ ] ProfileFavoriteWorkoutTemplateCrossRef
* [ ] WorkoutTemplate
* [ ] WorkoutTemplateDao
* [ ] WorkoutTemplateDetailUiState
* [ ] WorkoutTemplateDetailView
* [ ] WorkoutTemplateDetailViewModel
* [ ] WorkoutTemplateEntity
* [ ] WorkoutTemplateExercise
* [ ] WorkoutTemplateExerciseEntity
* [ ] WorkoutTemplateExerciseWithDetails
* [ ] WorkoutTemplateExerciseWithSets
* [ ] WorkoutTemplateRepository
* [ ] WorkoutTemplateSet
* [ ] WorkoutTemplateSetEntity
* [ ] WorkoutTemplateWithExercisesAndSets
* [ ] WorkoutTemplateWithExercisesEntity
* [ ] WorkoutTemplatesOverviewView
* [ ] WorkoutTemplatesOverviewViewModel
* [ ] WorkoutTemplatesUiState

##### workouts

* [ ] ExerciseExecutionView
* [ ] ExerciseExecutionViewModel
* [ ] WorkoutExecutionDao
* [ ] WorkoutExecutionEntity
* [ ] WorkoutExecutionRepository
* [ ] WorkoutExecutionStatus
* [ ] WorkoutExecutionWithSetsEntity
* [ ] WorkoutSetExecutionEntity
* [ ] WorkoutSetStatus

