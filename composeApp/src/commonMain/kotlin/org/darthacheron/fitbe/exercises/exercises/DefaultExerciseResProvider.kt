package org.darthacheron.fitbe.exercises.exercises

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.default_exercise_bench_press_guide
import fitbe.composeapp.generated.resources.default_exercise_bench_press_name
import fitbe.composeapp.generated.resources.default_exercise_burpees_guide
import fitbe.composeapp.generated.resources.default_exercise_burpees_name
import fitbe.composeapp.generated.resources.default_exercise_deadlift_guide
import fitbe.composeapp.generated.resources.default_exercise_deadlift_name
import fitbe.composeapp.generated.resources.default_exercise_jumping_jacks_guide
import fitbe.composeapp.generated.resources.default_exercise_jumping_jacks_name
import fitbe.composeapp.generated.resources.default_exercise_lunges_guide
import fitbe.composeapp.generated.resources.default_exercise_lunges_name
import fitbe.composeapp.generated.resources.default_exercise_mountain_climbers_guide
import fitbe.composeapp.generated.resources.default_exercise_mountain_climbers_name
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_guide
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_name
import fitbe.composeapp.generated.resources.default_exercise_plank_guide
import fitbe.composeapp.generated.resources.default_exercise_plank_name
import fitbe.composeapp.generated.resources.default_exercise_pull_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_pull_ups_name
import fitbe.composeapp.generated.resources.default_exercise_push_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_push_ups_name
import fitbe.composeapp.generated.resources.default_exercise_side_lunges_guide
import fitbe.composeapp.generated.resources.default_exercise_side_lunges_name
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_name
import fitbe.composeapp.generated.resources.default_exercise_squat_guide
import fitbe.composeapp.generated.resources.default_exercise_squat_name
import org.jetbrains.compose.resources.StringResource

object DefaultExerciseResProvider {
    val exerciseNameMap: Map<String, StringResource> = mapOf(
        "default_exercise_squat" to Res.string.default_exercise_squat_name,
        "default_exercise_deadlift" to Res.string.default_exercise_deadlift_name,
        "default_exercise_bench_press" to Res.string.default_exercise_bench_press_name,
        "default_exercise_overhead_press" to Res.string.default_exercise_overhead_press_name,
        "default_exercise_pull_ups" to Res.string.default_exercise_pull_ups_name,
        "default_exercise_push_ups" to Res.string.default_exercise_push_ups_name,
        "default_exercise_lunges" to Res.string.default_exercise_lunges_name,
        "default_exercise_sit_ups" to Res.string.default_exercise_sit_ups_name,
        "default_exercise_plank" to Res.string.default_exercise_plank_name,
        "default_exercise_jumping_jacks" to Res.string.default_exercise_jumping_jacks_name,
        "default_exercise_side_lunges" to Res.string.default_exercise_side_lunges_name,
        "default_exercise_burpees" to Res.string.default_exercise_burpees_name,
        "default_exercise_mountain_climbers" to Res.string.default_exercise_mountain_climbers_name,
    )

    val exerciseGuideMap: Map<String, StringResource> = mapOf(
        "default_exercise_squat" to Res.string.default_exercise_squat_guide,
        "default_exercise_deadlift" to Res.string.default_exercise_deadlift_guide,
        "default_exercise_bench_press" to Res.string.default_exercise_bench_press_guide,
        "default_exercise_overhead_press" to Res.string.default_exercise_overhead_press_guide,
        "default_exercise_pull_ups" to Res.string.default_exercise_pull_ups_guide,
        "default_exercise_push_ups" to Res.string.default_exercise_push_ups_guide,
        "default_exercise_lunges" to Res.string.default_exercise_lunges_guide,
        "default_exercise_sit_ups" to Res.string.default_exercise_sit_ups_guide,
        "default_exercise_plank" to Res.string.default_exercise_plank_guide,
        "default_exercise_jumping_jacks" to Res.string.default_exercise_jumping_jacks_guide,
        "default_exercise_side_lunges" to Res.string.default_exercise_side_lunges_guide,
        "default_exercise_burpees" to Res.string.default_exercise_burpees_guide,
        "default_exercise_mountain_climbers" to Res.string.default_exercise_mountain_climbers_guide,

    )
}