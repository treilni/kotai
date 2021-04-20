package com.treil.kotai.creature

import com.treil.kotai.brain.InputLayer
import com.treil.kotai.brain.StaticValue
import com.treil.kotai.world.Direction
import com.treil.kotai.world.World
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
internal class DirectionActuatorTest {

    @Test
    fun act() {
        val world = World(10, 10)
        val creature = Creature("TEST", 100)
        world.placeThingAt(creature, 5, 5)
        creature.facing = Direction(45)

        val actuator = DirectionActuator()
        val inputLayer = InputLayer()
        inputLayer.elements.add(StaticValue(0))
        actuator.inputLayer = inputLayer
        actuator.act(world, creature)
        assertEquals(Direction(45), creature.facing)

        inputLayer.elements.clear()
        inputLayer.elements.add(StaticValue(Short.MAX_VALUE))
        actuator.inputLayer = inputLayer
        actuator.act(world, creature)
        assertEquals(Direction(45 + 180), creature.facing)

        inputLayer.elements.clear()
        inputLayer.elements.add(StaticValue(Short.MIN_VALUE))
        actuator.inputLayer = inputLayer
        actuator.act(world, creature)
        assertEquals(Direction(45), creature.facing)
    }
}