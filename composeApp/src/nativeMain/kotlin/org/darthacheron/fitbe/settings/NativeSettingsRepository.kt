package org.darthacheron.fitbe.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.Foundation.NSDictionary
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dictionaryWithContentsOfFile
import platform.Foundation.writeToFile
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class NativeSettingsRepository : SettingsRepository {
    private val settingsFlow = MutableStateFlow(Settings())
    private val fileManager = NSFileManager.defaultManager
    private val settingsFile: String
        get() {
            val docs =
                NSSearchPathForDirectoriesInDomains(
                    NSDocumentDirectory,
                    NSUserDomainMask,
                    true
                ).first() as String
            return "$docs/${SettingsKeys.FILE_NAME}.plist"
        }

    init {
        loadSettings()
    }

    private fun loadSettings() {
        runCatching {
            if (!fileManager.fileExistsAtPath(settingsFile)) {
                return
            }

            (NSDictionary.dictionaryWithContentsOfFile(settingsFile))?.let { dict ->
                val selectedProfileIdStr =
                    dict.getValue(SettingsKeys.SELECTED_PROFILE_ID) as? String
                val selectedProfileId =
                    selectedProfileIdStr?.takeIf { it.isNotBlank() }?.let { Uuid.parse(it) }

                val settings =
                    Settings(
                        weightUnit =
                            (dict.getValue(SettingsKeys.WEIGHT_UNIT) as? String)?.let {
                                WeightUnit.valueOf(it)
                            } ?: WeightUnit.KG,
                        distanceUnit =
                            (dict.getValue(SettingsKeys.DISTANCE_UNIT) as? String)?.let {
                                DistanceUnit.valueOf(it)
                            } ?: DistanceUnit.KM,
                        bodyMeasurementUnit =
                            (dict.getValue(SettingsKeys.BODY_MEASUREMENT_UNIT) as? String)?.let {
                                BodyMeasurementUnit.valueOf(it)
                            } ?: BodyMeasurementUnit.CM,
                        themeMode =
                            (dict.getValue(SettingsKeys.THEME_MODE) as? String)?.let {
                                ThemeMode.valueOf(it)
                            } ?: ThemeMode.SYSTEM,
                        selectedProfileId = selectedProfileId
                    )
                settingsFlow.value = settings
            }
        }.onFailure { it.printStackTrace() }
    }

    override suspend fun saveSettings(settings: Settings) {
        runCatching {
            val dict =
                mutableMapOf(
                    SettingsKeys.WEIGHT_UNIT to settings.weightUnit.name,
                    SettingsKeys.DISTANCE_UNIT to settings.distanceUnit.name,
                    SettingsKeys.BODY_MEASUREMENT_UNIT to settings.bodyMeasurementUnit.name,
                    SettingsKeys.THEME_MODE to settings.themeMode.name,
                    SettingsKeys.SELECTED_PROFILE_ID to (
                        settings.selectedProfileId?.toString() ?: ""
                    )
                )

            (dict as NSDictionary).writeToFile(settingsFile, true)
            settingsFlow.value = settings
        }.onFailure { it.printStackTrace() }
    }

    override fun getSettingsFlow(): Flow<Settings> = settingsFlow

    override suspend fun getSettings(): Settings = settingsFlow.value
}