package com.treil.kotai.creature

import com.treil.kotai.world.World
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
internal class EyeSensorTest {

    @Test
    fun computeValue() {
        val world = World(10, 10)
        val creature = Creature(100)
        val eyeSensor = EyeSensor()

        world.placeThingAt(creature, 2, 8)
        eyeSensor.computeValue(world, creature)
        assertEquals(Short.MAX_VALUE, eyeSensor.inputs[0].value)

        world.placeThingAt(creature, 2, 7)
        eyeSensor.computeValue(world, creature)
        assertEquals((Short.MAX_VALUE - (Short.MAX_VALUE / 3)).toShort(), eyeSensor.inputs[0].value)

        world.placeThingAt(creature, 2, 6)
        eyeSensor.computeValue(world, creature)
        assertEquals((Short.MAX_VALUE - 2 * (Short.MAX_VALUE / 3)).toShort(), eyeSensor.inputs[0].value)

        world.placeThingAt(creature, 2, 5)
        eyeSensor.computeValue(world, creature)
        assertEquals(0, eyeSensor.inputs[0].value)
    }
}