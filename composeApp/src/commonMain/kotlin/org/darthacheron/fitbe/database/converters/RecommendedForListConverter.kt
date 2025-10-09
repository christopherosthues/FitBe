package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import org.darthacheron.fitbe.workouts.exercises.RecommendedFor

class RecommendedForListConverter {
    @TypeConverter
    fun fromRecommendedForList(recommendedForList: List<RecommendedFor>?): String? =
        recommendedForList?.joinToString(",") { it.name }

    @TypeConverter
    fun toRecommendedForList(data: String?): List<RecommendedFor>? =
        data?.split(",")?.mapNotNull {
            try {
                RecommendedFor.valueOf(it)
            } catch (e: IllegalArgumentException) {
                // Handle cases where an enum name might not be found (e.g., data corruption or enum changes)
                // Depending on requirements, you might log this error or skip the invalid entry.
                null
            }
        }
}