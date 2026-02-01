package org.darthacheron.fitbe.db

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.darthacheron.fitbe.database.FitBeDatabase
import org.darthacheron.fitbe.database.FitBeDatabase_Impl

/**
 * Actual implementation for the JVM (Desktop).
 * It creates an in-memory database. If you want it to be a file that gets deleted,
 * you can provide a file path. `null` makes it purely in-memory.
 */
actual fun inMemoryDatabaseFactory(): RoomDatabase.Builder<FitBeDatabase> {
    return Room
        .inMemoryDatabaseBuilder<FitBeDatabase>(
            factory = ::FitBeDatabase_Impl
        ).setDriver(BundledSQLiteDriver()) // The Driver is from the sqlite-bundled artifact for JVM
}