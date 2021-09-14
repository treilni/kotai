package com.treil.kotai.creature

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.treil.kotai.brain.InputLayer
import com.treil.kotai.brain.StaticValue
import com.treil.kotai.evolution.MovementScoreKeeper
import com.treil.kotai.world.Point2D
import com.treil.kotai.world.World
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
internal class MovementActuatorTest {
    init {
        val root: Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
        root.level = Level.DEBUG
    }

    @Test
    fun act() {
        val world = World(10, 10)
        val creature = Creature("TEST", 100, MovementScoreKeeper())
        world.placeThingAt(creature, 5, 5)

        val actuator = MovementActuator(MovementScoreKeeper())
        val inputLayer = InputLayer()
        inputLayer.elements.add(StaticValue(0))
        actuator.inputLayer = inputLayer
        actuator.act(world, creature)
        assertEquals(Point2D(5, 5), creature.location as Point2D)

        inputLayer.elements.clear()
        inputLayer.elements.add(StaticValue(Short.MAX_VALUE))
        actuator.inputLayer = inputLayer
        actuator.act(world, creature)
        assertEquals(Point2D(5, 7), creature.location as Point2D)

        inputLayer.elements.clear()
        inputLayer.elements.add(StaticValue(Short.MIN_VALUE))
        actuator.inputLayer = inputLayer
        actuator.act(world, creature)
        assertEquals(Point2D(5, 6), creature.location as Point2D)

    }
}