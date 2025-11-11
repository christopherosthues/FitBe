package org.darthacheron.fitbe.settings.export

import org.darthacheron.fitbe.health.beverages.BeverageRepository
import org.darthacheron.fitbe.health.sleep.SleepRepository
import org.darthacheron.fitbe.health.steps.StepsRepository
import org.darthacheron.fitbe.health.weight.BodyWeightRepository
import org.darthacheron.fitbe.workouts.equipment.EquipmentRepository
import org.darthacheron.fitbe.workouts.exercises.ExerciseRepository

class ExportService(
    beverageRepository: BeverageRepository,
    stepsRepository: StepsRepository,
    sleepRepository: SleepRepository,
    bodyWeightRepository: BodyWeightRepository,
    exerciseRepository: ExerciseRepository,
    equipmentRepository: EquipmentRepository,
) {
    fun export() {

    }
}