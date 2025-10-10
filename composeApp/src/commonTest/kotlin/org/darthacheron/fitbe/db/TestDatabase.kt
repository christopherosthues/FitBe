package org.darthacheron.fitbe.db

import androidx.room.RoomDatabase
import org.darthacheron.fitbe.database.FitBeDatabase

expect fun inMemoryDatabaseFactory(): RoomDatabase.Builder<FitBeDatabase>

fun createInMemoryRoomDatabase(): FitBeDatabase = inMemoryDatabaseFactory().build()