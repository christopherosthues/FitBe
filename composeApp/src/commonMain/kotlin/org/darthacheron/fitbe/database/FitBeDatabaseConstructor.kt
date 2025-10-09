package org.darthacheron.fitbe.database

import androidx.room.RoomDatabaseConstructor

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING,NO_ACTUAL_FOR_EXPECT")
expect object FitBeDatabaseConstructor : RoomDatabaseConstructor<FitBeDatabase> {
    override fun initialize(): FitBeDatabase
}