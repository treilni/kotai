package com.treil.kotai.world

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNotEquals

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
internal class WorldTest {
    class TestThing : Thing() {
    }

    @Test
    fun create() {
        val world = World(5, 6)

        for (x in 0..4) {
            val firstRowCell = world.getLocation(x, 0)
            assertNotEquals(null, firstRowCell?.getOccupant())
            val lastRowCell = world.getLocation(x, 5)
            assertNotEquals(null, lastRowCell?.getOccupant())
        }
        assertEquals(null, world.getLocation(1, 1)?.getOccupant())
        assertEquals(null, world.getLocation(3, 4)?.getOccupant())
    }

    @Test
    fun placeThingAt() {
        val world = World(5, 6)

        val thing = TestThing()
        world.placeThingAt(thing, 2, 3)
        assertEquals(thing, world.getLocation(2, 3)?.getOccupant())
    }

    @Test
    fun placeThingAtOccupied() {
        val world = World(5, 6)

        val thing = TestThing()
        assertThrows<IllegalAccessException> { world.placeThingAt(thing, 1, 0) }
    }

    @Test
    fun placeThingAtOccupied2() {
        val world = World(5, 6)

        val thing = TestThing()
        assertThrows<IllegalAccessException> { world.placeThingAt(thing, 1, 0) }
    }
}