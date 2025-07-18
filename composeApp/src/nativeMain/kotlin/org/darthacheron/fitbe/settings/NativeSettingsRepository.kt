package org.darthacheron.fitbe.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.Foundation.*

class NativeSettingsRepository : SettingsRepository {
    private val settingsFlow = MutableStateFlow(Settings())
    private val fileManager = NSFileManager.defaultManager
    private val settingsFile: String
        get() {
            val docs = NSSearchPathForDirectoriesInDomains(
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
            if (!fileManager.fileExistsAtPath(settingsFile)) return

            (NSDictionary.dictionaryWithContentsOfFile(settingsFile))?.let { dict ->
                val settings = Settings(
                    weightUnit = (dict.getValue(SettingsKeys.WEIGHT_UNIT) as? String)?.let {
                        WeightUnit.valueOf(it)
                    } ?: WeightUnit.KG,
                    distanceUnit = (dict.getValue(SettingsKeys.DISTANCE_UNIT) as? String)?.let {
                        DistanceUnit.valueOf(it)
                    } ?: DistanceUnit.KM,
                    themeMode = (dict.getValue(SettingsKeys.THEME_MODE) as? String)?.let {
                        ThemeMode.valueOf(it)
                    } ?: ThemeMode.SYSTEM
                )
                settingsFlow.value = settings
            }
        }.onFailure { it.printStackTrace() }
    }

    override suspend fun saveSettings(settings: Settings) {
        runCatching {
            val dict = mutableMapOf(
                SettingsKeys.WEIGHT_UNIT to settings.weightUnit.name,
                SettingsKeys.DISTANCE_UNIT to settings.distanceUnit.name,
                SettingsKeys.THEME_MODE to settings.themeMode.name
            )

            (dict as NSDictionary).writeToFile(settingsFile, true)
            settingsFlow.value = settings
        }.onFailure { it.printStackTrace() }
    }

    override fun getSettingsFlow(): Flow<Settings> = settingsFlow
    override suspend fun getSettings(): Settings = settingsFlow.value
}

