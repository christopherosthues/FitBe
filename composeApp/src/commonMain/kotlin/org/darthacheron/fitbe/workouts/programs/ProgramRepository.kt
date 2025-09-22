package org.darthacheron.fitbe.workouts.programs

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProgramRepository(programDao: ProgramDao) {
    fun getAllProgramsWithWorkouts(): Flow<List<Program>> {
        return flowOf(listOf(Program(name = "Program 1"), Program(name = "Program 2")))
    }

    fun getFavoriteProgramIds(profileId: Uuid): Flow<List<Uuid>> {
        return flowOf(listOf(Uuid.random(), Uuid.random()))
    }

    fun addFavorite(profileId: Uuid, programId: Uuid) {
        TODO("Not yet implemented")
    }

    fun removeFavorite(profileId: Uuid, programId: Uuid) {
        TODO("Not yet implemented")
    }
}