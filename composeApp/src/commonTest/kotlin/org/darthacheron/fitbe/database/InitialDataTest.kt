package org.darthacheron.fitbe.database

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class InitialDataTest {

    @Test
    fun `equipmentList should not have duplicate IDs`() {
        val ids = equipmentList.map { it.id }
        val distinctIds = ids.distinct()
        assertEquals(ids.size, distinctIds.size, "Duplicate IDs found in equipmentList")
    }

    @Test
    fun `equipmentList should not have duplicate keys`() {
        val keys = equipmentList.map { it.key }
        val distinctKeys = keys.distinct()
        assertEquals(keys.size, distinctKeys.size, "Duplicate keys found in equipmentList")
    }

    @Test
    fun `exerciseList should not have duplicate IDs`() {
        val ids = exerciseList.map { it.id }
        val distinctIds = ids.distinct()
        assertEquals(ids.size, distinctIds.size, "Duplicate IDs found in exerciseList")
    }

    @Test
    fun `exerciseList should not have duplicate keys`() {
        val keys = exerciseList.map { it.key }
        val distinctKeys = keys.distinct()
        assertEquals(keys.size, distinctKeys.size, "Duplicate keys found in exerciseList")
    }

    @Test
    fun `all equipmentKeys in exerciseList should exist in equipmentList`() {
        val equipmentKeys = equipmentList.map { it.key }.toSet()
        val missingKeys = exerciseList
            .flatMap { it.equipmentKeys }
            .filter { it !in equipmentKeys }
            .distinct()

        assertEquals(0, missingKeys.size, "Missing equipment keys found: ${missingKeys.joinToString()}")
    }
}
