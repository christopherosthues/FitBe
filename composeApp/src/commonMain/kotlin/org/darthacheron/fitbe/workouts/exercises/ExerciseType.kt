package org.darthacheron.fitbe.workouts.exercises

enum class ExerciseType {
    /** Exercises tracked by weight and repetitions (e.g., Bench Press, Squats with weights) */
    WEIGHT_REPS,

    /** Exercises tracked by repetitions only (e.g., Push Ups, Sit Ups, Bodyweight Squats) */
    REPS_ONLY,

    /** Exercises tracked by duration (e.g., Plank, Wall Sit, Stretching) */
    TIMED,

    /** Exercises tracked by distance and optionally duration (e.g., Running, Cycling, Rowing) */
    DISTANCE,

    /** Exercises tracked by weight and duration (e.g., Weighted Plank, Farmer's Walk) */
    WEIGHT_TIMED,

    /** Exercises tracked by weight, repetitions, and duration (e.g., AMRAP for X minutes with Y weight, or N reps with Z weight for time) */
    WEIGHT_REPS_TIMED,

    /** Exercises tracked by both distance and duration (e.g., Running 5km in 25 minutes, Cycling for 1 hour covering 20km) */
    DISTANCE_TIMED,

    /** Exercises where only completion is tracked, or the tracking type is not specific or varies greatly. */
    COMPLETION_ONLY,

    /** For any other type of exercise or for exercises where tracking metrics are not predefined. */
    OTHER
}