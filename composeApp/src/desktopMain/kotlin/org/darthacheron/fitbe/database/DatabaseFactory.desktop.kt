package org.darthacheron.fitbe.database

import androidx.room.Room
import java.io.File

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseFactory {
    actual fun create(): androidx.room.RoomDatabase.Builder<FitBeDatabase> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir =
            when {
                os.contains("win") -> File(System.getenv("APPDATA"), "FitBe")
                os.contains("mac") -> File(userHome, "Library/Application Support/FitBe")
                else -> File(userHome, ".local/share/FitBe")
            }

        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val dbFile = File(appDataDir, FitBeDatabase.DB_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}