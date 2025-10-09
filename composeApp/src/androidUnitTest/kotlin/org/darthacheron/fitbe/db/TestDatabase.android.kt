package org.darthacheron.fitbe.db

import androidx.room.RoomDatabase
import org.darthacheron.fitbe.database.FitBeDatabase

/**
 * Actual implementation for Android.
 * It uses the Android-specific Room.inMemoryDatabaseBuilder and requires an Android Context.
 */
actual fun inMemoryDatabaseFactory(): RoomDatabase.Builder<FitBeDatabase> {
//    val context = ApplicationProvider.getApplicationContext<Context>()
//    return Room.inMemoryDatabaseBuilder(context, FitBeDatabase::class)
    TODO("Not yet implemented")
}