package org.darthacheron.fitbe.database

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.exercises.ExerciseDao
import org.darthacheron.fitbe.exercises.TrainingEquipmentEntity
import org.darthacheron.fitbe.exercises.fromTrainingEquipmentEntity
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class PrepopulateCallback(
    private val exerciseDao: ExerciseDao
) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(connection: SQLiteConnection) {
        super.onCreate(connection)
        applicationScope.launch {
            populateDatabase(exerciseDao)
        }
    }

    private suspend fun populateDatabase(exerciseDao: ExerciseDao) {
        // --- BARBELL ---
        val barbell = TrainingEquipmentEntity(
            name = "Barbell",
            default = true,
        )
        exerciseDao.upsertEquipment(barbell)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(barbell)
        )

        // --- DUMBBELL ---
        val dumbbell = TrainingEquipmentEntity(
            name = "Dumbbell",
            default = true,
        )
        exerciseDao.upsertEquipment(dumbbell)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(dumbbell)
        )

        // --- BODYWEIGHT ---
        val bodyweight = TrainingEquipmentEntity(
            name = "Bodyweight",
            default = true,
        )
        exerciseDao.upsertEquipment(bodyweight)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(bodyweight)
        )

        // --- KETTLEBELL ---
        val kettlebell = TrainingEquipmentEntity(
            name = "Kettlebell",
            default = true,
        )
        exerciseDao.upsertEquipment(kettlebell)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(kettlebell)
        )

        // --- DIP STATION ---
        val dipStation = TrainingEquipmentEntity(
            name = "Dip Station",
            default = true,
        )
        exerciseDao.upsertEquipment(dipStation)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(dipStation)
        )

        // --- BENCH ---
        val bench = TrainingEquipmentEntity(
            name = "Bench",
            default = true,
        )
        exerciseDao.upsertEquipment(bench)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(bench)
        )

        // --- ROWING MACHINE ---
        val rowingMachine = TrainingEquipmentEntity(
            name = "Rowing Machine",
            default = true,
        )
        exerciseDao.upsertEquipment(rowingMachine)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(rowingMachine)
        )

        // --- CHEST PRESS MACHINE ---
        val chestPressMachine = TrainingEquipmentEntity(
            name = "Chest Press Machine",
            default = true,
        )
        exerciseDao.upsertEquipment(chestPressMachine)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(chestPressMachine)
        )

        // --- EZ CURL BAR ---
        val ezCurlBar = TrainingEquipmentEntity(
            name = "EZ Curl Bar",
            default = true,
        )
        exerciseDao.upsertEquipment(ezCurlBar)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(ezCurlBar)
        )

        // --- WEIGHT PLATES ---
        val weightPlates = TrainingEquipmentEntity(
            name = "Weight Plates",
            default = true,
        )
        exerciseDao.upsertEquipment(weightPlates)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(weightPlates)
        )

        // --- ANKLE WEIGHTS ---
        val ankleWeights = TrainingEquipmentEntity(
            name = "Ankle Weights",
            default = true,
        )
        exerciseDao.upsertEquipment(ankleWeights)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(ankleWeights)
        )

        // --- WRIST WEIGHTS ---
        val wristWeights = TrainingEquipmentEntity(
            name = "Wrist Weights",
            default = true,
        )
        exerciseDao.upsertEquipment(wristWeights)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(wristWeights)
        )

        // --- CABLE MACHINE ---
        val cableMachine = TrainingEquipmentEntity(
            name = "Cable Machine",
            default = true,
        )
        exerciseDao.upsertEquipment(cableMachine)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(cableMachine)
        )

        // --- LAT PULLDOWN MACHINE ---
        val latPulldownMachine = TrainingEquipmentEntity(
            name = "Lat Pulldown Machine",
            default = true,
        )
        exerciseDao.upsertEquipment(latPulldownMachine)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(latPulldownMachine)
        )

        // --- LEG PRESS MACHINE ---
        val legPressMachine = TrainingEquipmentEntity(
            name = "Leg Press Machine",
            default = true,
        )
        exerciseDao.upsertEquipment(legPressMachine)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(legPressMachine)
        )

        // --- LEG EXTENSION MACHINE ---
        val legExtensionMachine = TrainingEquipmentEntity(
            name = "Leg Extension Machine",
            default = true,
        )
        exerciseDao.upsertEquipment(legExtensionMachine)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(legExtensionMachine)
        )

        // --- LEG CURL MACHINE ---
        val legCurlMachine = TrainingEquipmentEntity(
            name = "Leg Curl Machine",
            default = true,
        )
        exerciseDao.upsertEquipment(legCurlMachine)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(legCurlMachine)
        )

        // --- SMITH MACHINE ---
        val smithMachine = TrainingEquipmentEntity(
            name = "Smith Machine",
            default = true,
        )
        exerciseDao.upsertEquipment(smithMachine)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(smithMachine)
        )

        // --- SHOULDER PRESS MACHINE ---
        val shoulderPressMachine = TrainingEquipmentEntity(
            name = "Shoulder Press Machine",
            default = true,
        )
        exerciseDao.upsertEquipment(shoulderPressMachine)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(shoulderPressMachine)
        )

        // --- SEATED ROW MACHINE ---
        val seatedRowMachine = TrainingEquipmentEntity(
            name = "Seated Row Machine",
            default = true,
        )
        exerciseDao.upsertEquipment(seatedRowMachine)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(seatedRowMachine)
        )

        // --- PULL-UP BAR ---
        val pullUpBar = TrainingEquipmentEntity(
            name = "Pull-up Bar",
            default = true,
        )
        exerciseDao.upsertEquipment(pullUpBar)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(pullUpBar)
        )

        // --- DIP BARS ---
        val dipBars = TrainingEquipmentEntity( // Note: "Dip Station" already exists, this could be a duplicate or a more specific type
            name = "Dip Bars",
            default = true,
        )
        exerciseDao.upsertEquipment(dipBars)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(dipBars)
        )

        // --- PUSH-UP HANDLES ---
        val pushUpHandles = TrainingEquipmentEntity(
            name = "Push-up Handles",
            default = true,
        )
        exerciseDao.upsertEquipment(pushUpHandles)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(pushUpHandles)
        )

        // --- PARALLETTES ---
        val parallettes = TrainingEquipmentEntity(
            name = "Parallettes",
            default = true,
        )
        exerciseDao.upsertEquipment(parallettes)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(parallettes)
        )

        // --- GYMNASTIC RINGS ---
        val gymnasticRings = TrainingEquipmentEntity(
            name = "Gymnastic Rings",
            default = true,
        )
        exerciseDao.upsertEquipment(gymnasticRings)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(gymnasticRings)
        )

        // --- SUSPENSION TRAINER ---
        val suspensionTrainer = TrainingEquipmentEntity(
            name = "Suspension Trainer",
            default = true,
        )
        exerciseDao.upsertEquipment(suspensionTrainer)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(suspensionTrainer)
        )

        // --- PLYO BOX ---
        val plyoBox = TrainingEquipmentEntity(
            name = "Plyo Box",
            default = true,
        )
        exerciseDao.upsertEquipment(plyoBox)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(plyoBox)
        )

        // --- MEDICINE BALL ---
        val medicineBall = TrainingEquipmentEntity(
            name = "Medicine Ball",
            default = true,
        )
        exerciseDao.upsertEquipment(medicineBall)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(medicineBall)
        )

        // --- SLAM BALL ---
        val slamBall = TrainingEquipmentEntity(
            name = "Slam Ball",
            default = true,
        )
        exerciseDao.upsertEquipment(slamBall)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(slamBall)
        )

        // --- WALL BALL ---
        val wallBall = TrainingEquipmentEntity(
            name = "Wall Ball",
            default = true,
        )
        exerciseDao.upsertEquipment(wallBall)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(wallBall)
        )

        // --- BATTLE ROPES ---
        val battleRopes = TrainingEquipmentEntity(
            name = "Battle Ropes",
            default = true,
        )
        exerciseDao.upsertEquipment(battleRopes)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(battleRopes)
        )

        // --- SANDBAG ---
        val sandbag = TrainingEquipmentEntity(
            name = "Sandbag",
            default = true,
        )
        exerciseDao.upsertEquipment(sandbag)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(sandbag)
        )

        // --- BULGARIAN BAG ---
        val bulgarianBag = TrainingEquipmentEntity(
            name = "Bulgarian Bag",
            default = true,
        )
        exerciseDao.upsertEquipment(bulgarianBag)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(bulgarianBag)
        )

        // --- RESISTANCE BANDS ---
        val resistanceBands = TrainingEquipmentEntity(
            name = "Resistance Bands",
            default = true,
        )
        exerciseDao.upsertEquipment(resistanceBands)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(resistanceBands)
        )

        // --- FOAM ROLLER ---
        val foamRoller = TrainingEquipmentEntity(
            name = "Foam Roller",
            default = true,
        )
        exerciseDao.upsertEquipment(foamRoller)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(foamRoller)
        )

        // --- BALANCE PAD ---
        val balancePad = TrainingEquipmentEntity(
            name = "Balance Pad",
            default = true,
        )
        exerciseDao.upsertEquipment(balancePad)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(balancePad)
        )

        // --- BOSU BALL ---
        val bosuBall = TrainingEquipmentEntity(
            name = "Bosu Ball",
            default = true,
        )
        exerciseDao.upsertEquipment(bosuBall)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(bosuBall)
        )

        // --- TREADMILL ---
        val treadmill = TrainingEquipmentEntity(
            name = "Treadmill",
            default = true,
        )
        exerciseDao.upsertEquipment(treadmill)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(treadmill)
        )

        // --- ELLIPTICAL TRAINER ---
        val ellipticalTrainer = TrainingEquipmentEntity(
            name = "Elliptical Trainer",
            default = true,
        )
        exerciseDao.upsertEquipment(ellipticalTrainer)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(ellipticalTrainer)
        )

        // --- STATIONARY BIKE ---
        val stationaryBike = TrainingEquipmentEntity(
            name = "Stationary Bike",
            default = true,
        )
        exerciseDao.upsertEquipment(stationaryBike)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(stationaryBike)
        )

        // --- STAIR CLIMBER ---
        val stairClimber = TrainingEquipmentEntity(
            name = "Stair Climber",
            default = true,
        )
        exerciseDao.upsertEquipment(stairClimber)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(stairClimber)
        )

        // --- AIR BIKE ---
        val airBike = TrainingEquipmentEntity(
            name = "Air Bike",
            default = true,
        )
        exerciseDao.upsertEquipment(airBike)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(airBike)
        )

        // --- POWER RACK ---
        val powerRack = TrainingEquipmentEntity(
            name = "Power Rack",
            default = true,
        )
        exerciseDao.upsertEquipment(powerRack)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(powerRack)
        )

        // --- SQUAT RACK ---
        val squatRack = TrainingEquipmentEntity(
            name = "Squat Rack",
            default = true,
        )
        exerciseDao.upsertEquipment(squatRack)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(squatRack)
        )

        // --- SPOTTER ARMS ---
        val spotterArms = TrainingEquipmentEntity(
            name = "Spotter Arms",
            default = true,
        )
        exerciseDao.upsertEquipment(spotterArms)
        exerciseDao.insertDefaultEquipment(
            fromTrainingEquipmentEntity(spotterArms)
        )

        // Add more default equipment as needed...
    }
}
