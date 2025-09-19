package org.darthacheron.fitbe.workouts.exercises

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.default_exercise_squat_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_squat_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_squat_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_squat_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_squat_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_squat_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_goblet_squat_kettlebell_name
import fitbe.composeapp.generated.resources.default_exercise_goblet_squat_kettlebell_guide
import fitbe.composeapp.generated.resources.default_exercise_deadlift_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_deadlift_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_romanian_deadlift_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_romanian_deadlift_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_romanian_deadlift_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_romanian_deadlift_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_bench_press_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_bench_press_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_bench_press_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_bench_press_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_pull_ups_name
import fitbe.composeapp.generated.resources.default_exercise_pull_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_push_ups_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_push_ups_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_push_ups_handles_name
import fitbe.composeapp.generated.resources.default_exercise_push_ups_handles_guide
import fitbe.composeapp.generated.resources.default_exercise_lunges_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_lunges_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_lunges_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_lunges_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_lunges_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_lunges_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_name
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_plank_name
import fitbe.composeapp.generated.resources.default_exercise_plank_guide
import fitbe.composeapp.generated.resources.default_exercise_jumping_jacks_name
import fitbe.composeapp.generated.resources.default_exercise_jumping_jacks_guide
import fitbe.composeapp.generated.resources.default_exercise_side_lunges_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_side_lunges_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_side_lunges_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_side_lunges_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_burpees_name
import fitbe.composeapp.generated.resources.default_exercise_burpees_guide
import fitbe.composeapp.generated.resources.default_exercise_mountain_climbers_name
import fitbe.composeapp.generated.resources.default_exercise_mountain_climbers_guide
// Power Rack Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_rack_pulls_barbell_power_rack_name
import fitbe.composeapp.generated.resources.default_exercise_rack_pulls_barbell_power_rack_guide
import fitbe.composeapp.generated.resources.default_exercise_squat_barbell_power_rack_name
import fitbe.composeapp.generated.resources.default_exercise_squat_barbell_power_rack_guide
import fitbe.composeapp.generated.resources.default_exercise_bench_press_barbell_power_rack_name
import fitbe.composeapp.generated.resources.default_exercise_bench_press_barbell_power_rack_guide
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_barbell_power_rack_name
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_barbell_power_rack_guide
// Smith Machine Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_squat_smith_machine_name
import fitbe.composeapp.generated.resources.default_exercise_squat_smith_machine_guide
import fitbe.composeapp.generated.resources.default_exercise_bench_press_smith_machine_name
import fitbe.composeapp.generated.resources.default_exercise_bench_press_smith_machine_guide
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_smith_machine_name
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_smith_machine_guide
// Chest Press Machine Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_chest_press_machine_name
import fitbe.composeapp.generated.resources.default_exercise_chest_press_machine_guide
import fitbe.composeapp.generated.resources.ic_launcher
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

object DefaultExerciseResProvider {
    val exerciseNameMap: Map<String, StringResource> = mapOf(
        "default_exercise_squat_bodyweight" to Res.string.default_exercise_squat_bodyweight_name,
        "default_exercise_squat_barbell" to Res.string.default_exercise_squat_barbell_name,
        "default_exercise_squat_dumbbell" to Res.string.default_exercise_squat_dumbbell_name,
        "default_exercise_goblet_squat_kettlebell" to Res.string.default_exercise_goblet_squat_kettlebell_name,
        "default_exercise_deadlift_barbell" to Res.string.default_exercise_deadlift_barbell_name,
        "default_exercise_romanian_deadlift_dumbbell" to Res.string.default_exercise_romanian_deadlift_dumbbell_name,
        "default_exercise_romanian_deadlift_barbell" to Res.string.default_exercise_romanian_deadlift_barbell_name,
        "default_exercise_bench_press_barbell" to Res.string.default_exercise_bench_press_barbell_name,
        "default_exercise_bench_press_dumbbell" to Res.string.default_exercise_bench_press_dumbbell_name,
        "default_exercise_overhead_press_barbell" to Res.string.default_exercise_overhead_press_barbell_name,
        "default_exercise_overhead_press_dumbbell" to Res.string.default_exercise_overhead_press_dumbbell_name,
        "default_exercise_pull_ups" to Res.string.default_exercise_pull_ups_name,
        "default_exercise_push_ups_bodyweight" to Res.string.default_exercise_push_ups_bodyweight_name,
        "default_exercise_push_ups_handles" to Res.string.default_exercise_push_ups_handles_name,
        "default_exercise_lunges_bodyweight" to Res.string.default_exercise_lunges_bodyweight_name,
        "default_exercise_lunges_dumbbell" to Res.string.default_exercise_lunges_dumbbell_name,
        "default_exercise_lunges_barbell" to Res.string.default_exercise_lunges_barbell_name,
        "default_exercise_sit_ups" to Res.string.default_exercise_sit_ups_name,
        "default_exercise_plank" to Res.string.default_exercise_plank_name,
        "default_exercise_jumping_jacks" to Res.string.default_exercise_jumping_jacks_name,
        "default_exercise_side_lunges_bodyweight" to Res.string.default_exercise_side_lunges_bodyweight_name,
        "default_exercise_side_lunges_dumbbell" to Res.string.default_exercise_side_lunges_dumbbell_name,
        "default_exercise_burpees" to Res.string.default_exercise_burpees_name,
        "default_exercise_mountain_climbers" to Res.string.default_exercise_mountain_climbers_name,
        // Power Rack Exercises
        "default_exercise_rack_pulls_barbell_power_rack" to Res.string.default_exercise_rack_pulls_barbell_power_rack_name,
        "default_exercise_squat_barbell_power_rack" to Res.string.default_exercise_squat_barbell_power_rack_name,
        "default_exercise_bench_press_barbell_power_rack" to Res.string.default_exercise_bench_press_barbell_power_rack_name,
        "default_exercise_overhead_press_barbell_power_rack" to Res.string.default_exercise_overhead_press_barbell_power_rack_name,
        // Smith Machine Exercises
        "default_exercise_squat_smith_machine" to Res.string.default_exercise_squat_smith_machine_name,
        "default_exercise_bench_press_smith_machine" to Res.string.default_exercise_bench_press_smith_machine_name,
        "default_exercise_overhead_press_smith_machine" to Res.string.default_exercise_overhead_press_smith_machine_name,
        // Chest Press Machine Exercises
        "default_exercise_chest_press_machine" to Res.string.default_exercise_chest_press_machine_name
    )

    val exerciseGuideMap: Map<String, StringResource> = mapOf(
        "default_exercise_squat_bodyweight" to Res.string.default_exercise_squat_bodyweight_guide,
        "default_exercise_squat_barbell" to Res.string.default_exercise_squat_barbell_guide,
        "default_exercise_squat_dumbbell" to Res.string.default_exercise_squat_dumbbell_guide,
        "default_exercise_goblet_squat_kettlebell" to Res.string.default_exercise_goblet_squat_kettlebell_guide,
        "default_exercise_deadlift_barbell" to Res.string.default_exercise_deadlift_barbell_guide,
        "default_exercise_romanian_deadlift_dumbbell" to Res.string.default_exercise_romanian_deadlift_dumbbell_guide,
        "default_exercise_romanian_deadlift_barbell" to Res.string.default_exercise_romanian_deadlift_barbell_guide,
        "default_exercise_bench_press_barbell" to Res.string.default_exercise_bench_press_barbell_guide,
        "default_exercise_bench_press_dumbbell" to Res.string.default_exercise_bench_press_dumbbell_guide,
        "default_exercise_overhead_press_barbell" to Res.string.default_exercise_overhead_press_barbell_guide,
        "default_exercise_overhead_press_dumbbell" to Res.string.default_exercise_overhead_press_dumbbell_guide,
        "default_exercise_pull_ups" to Res.string.default_exercise_pull_ups_guide,
        "default_exercise_push_ups_bodyweight" to Res.string.default_exercise_push_ups_bodyweight_guide,
        "default_exercise_push_ups_handles" to Res.string.default_exercise_push_ups_handles_guide,
        "default_exercise_lunges_bodyweight" to Res.string.default_exercise_lunges_bodyweight_guide,
        "default_exercise_lunges_dumbbell" to Res.string.default_exercise_lunges_dumbbell_guide,
        "default_exercise_lunges_barbell" to Res.string.default_exercise_lunges_barbell_guide,
        "default_exercise_sit_ups" to Res.string.default_exercise_sit_ups_guide,
        "default_exercise_plank" to Res.string.default_exercise_plank_guide,
        "default_exercise_jumping_jacks" to Res.string.default_exercise_jumping_jacks_guide,
        "default_exercise_side_lunges_bodyweight" to Res.string.default_exercise_side_lunges_bodyweight_guide,
        "default_exercise_side_lunges_dumbbell" to Res.string.default_exercise_side_lunges_dumbbell_guide,
        "default_exercise_burpees" to Res.string.default_exercise_burpees_guide,
        "default_exercise_mountain_climbers" to Res.string.default_exercise_mountain_climbers_guide,
        // Power Rack Exercises
        "default_exercise_rack_pulls_barbell_power_rack" to Res.string.default_exercise_rack_pulls_barbell_power_rack_guide,
        "default_exercise_squat_barbell_power_rack" to Res.string.default_exercise_squat_barbell_power_rack_guide,
        "default_exercise_bench_press_barbell_power_rack" to Res.string.default_exercise_bench_press_barbell_power_rack_guide,
        "default_exercise_overhead_press_barbell_power_rack" to Res.string.default_exercise_overhead_press_barbell_power_rack_guide,
        // Smith Machine Exercises
        "default_exercise_squat_smith_machine" to Res.string.default_exercise_squat_smith_machine_guide,
        "default_exercise_bench_press_smith_machine" to Res.string.default_exercise_bench_press_smith_machine_guide,
        "default_exercise_overhead_press_smith_machine" to Res.string.default_exercise_overhead_press_smith_machine_guide,
        // Chest Press Machine Exercises
        "default_exercise_chest_press_machine" to Res.string.default_exercise_chest_press_machine_guide
    )

    val exerciseImageMap: Map<String, DrawableResource> = mapOf(
        "default_exercise_squat_bodyweight" to Res.drawable.ic_launcher,
        "default_exercise_squat_barbell" to Res.drawable.ic_launcher,
        "default_exercise_squat_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_goblet_squat_kettlebell" to Res.drawable.ic_launcher,
        "default_exercise_deadlift_barbell" to Res.drawable.ic_launcher,
        "default_exercise_romanian_deadlift_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_romanian_deadlift_barbell" to Res.drawable.ic_launcher,
        "default_exercise_bench_press_barbell" to Res.drawable.ic_launcher,
        "default_exercise_bench_press_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_overhead_press_barbell" to Res.drawable.ic_launcher,
        "default_exercise_overhead_press_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_pull_ups" to Res.drawable.ic_launcher,
        "default_exercise_push_ups_bodyweight" to Res.drawable.ic_launcher,
        "default_exercise_push_ups_handles" to Res.drawable.ic_launcher,
        "default_exercise_lunges_bodyweight" to Res.drawable.ic_launcher,
        "default_exercise_lunges_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_lunges_barbell" to Res.drawable.ic_launcher,
        "default_exercise_sit_ups" to Res.drawable.ic_launcher,
        "default_exercise_plank" to Res.drawable.ic_launcher,
        "default_exercise_jumping_jacks" to Res.drawable.ic_launcher,
        "default_exercise_side_lunges_bodyweight" to Res.drawable.ic_launcher,
        "default_exercise_side_lunges_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_burpees" to Res.drawable.ic_launcher,
        "default_exercise_mountain_climbers" to Res.drawable.ic_launcher,
        // Power Rack Exercises
        "default_exercise_rack_pulls_barbell_power_rack" to Res.drawable.ic_launcher,
        "default_exercise_squat_barbell_power_rack" to Res.drawable.ic_launcher,
        "default_exercise_bench_press_barbell_power_rack" to Res.drawable.ic_launcher,
        "default_exercise_overhead_press_barbell_power_rack" to Res.drawable.ic_launcher,
        // Smith Machine Exercises
        "default_exercise_squat_smith_machine" to Res.drawable.ic_launcher,
        "default_exercise_bench_press_smith_machine" to Res.drawable.ic_launcher,
        "default_exercise_overhead_press_smith_machine" to Res.drawable.ic_launcher,
        // Chest Press Machine Exercises
        "default_exercise_chest_press_machine" to Res.drawable.ic_launcher
    )
}
