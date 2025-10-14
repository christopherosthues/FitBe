package org.darthacheron.fitbe.health.weight

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverages_daily_view_error_loading
import fitbe.composeapp.generated.resources.body_weight_daily_view_content_description_add_body_weight
import fitbe.composeapp.generated.resources.body_weight_daily_view_error_loading
import fitbe.composeapp.generated.resources.body_weight_daily_view_error_saving
import fitbe.composeapp.generated.resources.body_weight_overview_error_loading
import fitbe.composeapp.generated.resources.top_bar_title_daily_body_weights
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.beverages.BeverageDailyError
import org.darthacheron.fitbe.health.beverages.BeverageDailyUiState
import org.darthacheron.fitbe.health.components.DailyViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.settings.converters.WeightUnitConverter
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.utils.roundToDecimals
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class BodyWeightDailyViewModel(
    private val repository: BodyWeightRepository,
    private val profileRepository: ProfileRepository,
    settingsRepository: SettingsRepository,
    private val weightUnitConverter: WeightUnitConverter,
    topBarManager: TopBarManager
) : DailyViewModel<BodyWeightDailyError, BodyWeightDailyUiState>(settingsRepository, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_daily_body_weights

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val addButtonContentDescription: StringResource
        get() = Res.string.body_weight_daily_view_content_description_add_body_weight

    private val settings: Flow<Settings> = settingsRepository.getSettingsFlow()

    private val targetBodyWeight: StateFlow<Double> =
        settingsRepository
            .getSettingsFlow()
            .flatMapLatest { settings ->
                val profileId = settings.selectedProfileId
                if (profileId != null) {
                    profileRepository
                        .getProfileFlowById(profileId)
                        .map { profile -> profile?.targetWeight ?: ProfileDefaults.WEIGHT_IN_KG }
                } else {
                    flowOf(ProfileDefaults.WEIGHT_IN_KG)
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.WEIGHT_IN_KG)

    private fun maxDefaultWeight(settings: Settings): Double {
        return weightUnitConverter.convert(ProfileDefaults.MAX_BODY_WEIGHT, WeightUnit.KG, settings.weightUnit)!!
    }

    private val bodyWeightFlow =
        date
            .flatMapLatest { date ->
                settingsRepository.getSettingsFlow().flatMapLatest { settings ->
                    settings.selectedProfileId?.let {
                        repository.getBodyWeights(date.toLocalDateTime(TimeZone.currentSystemDefault()).date, it)
                    } ?: flowOf(emptyList())
                }
            }.onStart {
                isLoading.value = true
                errorMessage.value = null
            }.catch {
                isLoading.value = false
                errorMessage.value = Res.string.body_weight_daily_view_error_loading
                emit(emptyList())
            }.map { beverages ->
                isLoading.value = false
                beverages
            }

    override val uiState: StateFlow<BodyWeightDailyUiState> =
        combine(
            bodyWeightFlow,
            targetBodyWeight,
            settings,
            isLoading,
            errorMessage
        ) { bodyWeights, target, settings, isLoading, error ->
            val weights = bodyWeights.map {
                BodyWeight(
                    id = it.id,
                    profileId = it.profileId,
                    date = it.date,
                    weightInKg =
                        weightUnitConverter
                            .convert(
                                it.weightInKg,
                                WeightUnit.KG,
                                settings.weightUnit
                            )?.roundToDecimals(2)!!,
                    muscleMassInKg =
                        weightUnitConverter
                            .convert(
                                it.muscleMassInKg,
                                WeightUnit.KG,
                                settings.weightUnit
                            )?.roundToDecimals(2),
                    boneMassInKg =
                        weightUnitConverter
                            .convert(
                                it.boneMassInKg,
                                WeightUnit.KG,
                                settings.weightUnit
                            )?.roundToDecimals(2),
                    bodyFatPercentage = it.bodyFatPercentage?.roundToDecimals(2),
                    bodyWaterInPercentage = it.bodyWaterInPercentage?.roundToDecimals(2)
                )

            }
            BodyWeightDailyUiState(
                isLoading = isLoading,
                bodyWeights = weights,
                target = target,
                weightUnit = settings.weightUnit,
                error = BodyWeightDailyError(error)
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BodyWeightDailyUiState())

    fun addBodyWeight(
        date: LocalDate,
        weightInKg: Double,
        bodyFatPercentage: Double?,
        muscleMassInKg: Double?,
        boneMassInKg: Double?,
        bodyWaterInPercentage: Double?
    ) {
        viewModelScope.launch {
            try {
                val profileId = settingsRepository.getSettings().selectedProfileId

                if (profileId == null) {
                    errorMessage.value = Res.string.body_weight_daily_view_error_saving
                    return@launch
                }

                repository.addBodyWeight(
                    BodyWeight(
                        profileId = profileId,
                        date = date.atStartOfDayIn(TimeZone.currentSystemDefault()),
                        weightInKg = weightInKg,
                        bodyFatPercentage = bodyFatPercentage,
                        muscleMassInKg = muscleMassInKg,
                        boneMassInKg = boneMassInKg,
                        bodyWaterInPercentage = bodyWaterInPercentage
                    )
                )
            } catch (e: Exception) {
                errorMessage.value = Res.string.body_weight_daily_view_error_saving
            }
        }
    }
}