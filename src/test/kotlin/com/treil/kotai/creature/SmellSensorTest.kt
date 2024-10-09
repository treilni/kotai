package com.treil.kotai.creature

import com.treil.kotai.evolution.MovementScoreKeeper
import com.treil.kotai.world.Food
import com.treil.kotai.world.World
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals

/**
 * @author Nicolas
 * @since 29/04/2021.
 */
@Suppress("LoggingSimilarMessage")
internal class SmellSensorTest {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(SmellSensorTest::class.java.simpleName)
    }

    /**
    6 XXXXXXX
    5 X  F  X
    4 X     X
    3 X  C  X
    2 X     X
    1 X     X
    0 XXXXXXX
    --0123456
     */
    @Test
    fun computeBearingR0() {
        val world = World(7, 7, 0, 0)
        val creature = Creature("C", 100, MovementScoreKeeper())
        val sensor = SmellSensor()
        creature.addComponent(sensor)
        Food(world.getLocation(3, 5)!!, 100)
        world.placeThingAt(creature, 3, 3)
        sensor.computeValue(world, creature)
        logger.info("Strength : ${sensor.getStrength()}")
        assertEquals(0, sensor.getRelativeBearing())
    }

    /**
    6 XXXXXXX
    5 X   F X
    4 X     X
    3 X  C  X
    2 X     X
    1 X     X
    0 XXXXXXX
    --0123456
     */
    @Test
    fun computeBearingR27() {
        val world = World(7, 7, 0, 0)
        val creature = Creature("C", 100, MovementScoreKeeper())
        val sensor = SmellSensor()
        creature.addComponent(sensor)
        Food(world.getLocation(4, 5)!!, 100)
        world.placeThingAt(creature, 3, 3)
        sensor.computeValue(world, creature)
        logger.info("Strength : ${sensor.getStrength()}")
        assertEquals(-27, sensor.getRelativeBearing())
    }

    /**
    6 XXXXXXX
    5 X     X
    4 X   F X
    3 X  C  X
    2 X     X
    1 X     X
    0 XXXXXXX
    --0123456
     */
    @Test
    fun computeBearingR45() {
        val world = World(7, 7, 0, 0)
        val creature = Creature("C", 100, MovementScoreKeeper())
        val sensor = SmellSensor()
        creature.addComponent(sensor)
        Food(world.getLocation(4, 4)!!, 100)
        world.placeThingAt(creature, 3, 3)
        sensor.computeValue(world, creature)
        logger.info("Strength : ${sensor.getStrength()}")
        assertEquals(-45, sensor.getRelativeBearing())
    }

    /**
    6 XXXXXXX
    5 X     X
    4 X    FX
    3 X  C  X
    2 X     X
    1 X     X
    0 XXXXXXX
    --0123456
     */
    @Test
    fun computeBearingR60() {
        val world = World(7, 7, 0, 0)
        val creature = Creature("C", 100, MovementScoreKeeper())
        val sensor = SmellSensor()
        creature.addComponent(sensor)
        Food(world.getLocation(5, 4)!!, 100)
        world.placeThingAt(creature, 3, 3)
        sensor.computeValue(world, creature)
        logger.info("Strength : ${sensor.getStrength()}")
        assertEquals(-63, sensor.getRelativeBearing())
    }


    /**
    6 XXXXXXX
    5 X     X
    4 X     X
    3 X  C FX
    2 X     X
    1 X     X
    0 XXXXXXX
    --0123456
     */
    @Test
    fun computeBearingR90() {
        val world = World(7, 7, 0, 0)
        val creature = Creature("C", 100, MovementScoreKeeper())
        val sensor = SmellSensor()
        creature.addComponent(sensor)
        Food(world.getLocation(5, 3)!!, 100)
        world.placeThingAt(creature, 3, 3)
        sensor.computeValue(world, creature)
        logger.info("Strength : ${sensor.getStrength()}")
        assertEquals(-90, sensor.getRelativeBearing())
    }

    /**
    6 XXXXXXX
    5 X     X
    4 X     X
    3 X  C  X
    2 X     X
    1 X    FX
    0 XXXXXXX
    --0123456
     */
    @Test
    fun computeBearingR135() {
        val world = World(7, 7, 0, 0)
        val creature = Creature("C", 100, MovementScoreKeeper())
        val sensor = SmellSensor()
        creature.addComponent(sensor)
        Food(world.getLocation(5, 1)!!, 100)
        world.placeThingAt(creature, 3, 3)
        sensor.computeValue(world, creature)
        logger.info("Strength : ${sensor.getStrength()}")
        assertEquals(-135, sensor.getRelativeBearing())
    }


    /**
    6 XXXXXXX
    5 X     X
    4 X F   X
    3 X  C  X
    2 X     X
    1 X     X
    0 XXXXXXX
    --0123456
     */
    @Test
    fun computeBearingL45() {
        val world = World(7, 7, 0, 0)
        val creature = Creature("C", 100, MovementScoreKeeper())
        val sensor = SmellSensor()
        creature.addComponent(sensor)
        Food(world.getLocation(2, 4)!!, 100)
        world.placeThingAt(creature, 3, 3)
        sensor.computeValue(world, creature)
        logger.info("Strength : ${sensor.getStrength()}")
        assertEquals(45, sensor.getRelativeBearing())
    }

    /**
    6 XXXXXXX
    5 X     X
    4 X     X
    3 X FC  X
    2 X     X
    1 X     X
    0 XXXXXXX
    --0123456
     */
    @Test
    fun computeBearingL90() {
        val world = World(7, 7, 0, 0)
        val creature = Creature("C", 100, MovementScoreKeeper())
        val sensor = SmellSensor()
        creature.addComponent(sensor)
        Food(world.getLocation(2, 3)!!, 100)
        world.placeThingAt(creature, 3, 3)
        sensor.computeValue(world, creature)
        logger.info("Strength : ${sensor.getStrength()}")
        assertEquals(90, sensor.getRelativeBearing())
    }

    /**
    6 XXXXXXX
    5 X     X
    4 X     X
    3 X  C  X
    2 X     X
    1 X F   X
    0 XXXXXXX
    --0123456
     */
    @Test
    fun computeBearing153() {
        val world = World(7, 7, 0, 0)
        val creature = Creature("C", 100, MovementScoreKeeper())
        val sensor = SmellSensor()
        creature.addComponent(sensor)
        Food(world.getLocation(2, 1)!!, 100)
        world.placeThingAt(creature, 3, 3)
        sensor.computeValue(world, creature)
        logger.info("Strength : ${sensor.getStrength()}")
        assertEquals(153, sensor.getRelativeBearing())
    }

    /**
    6 XXXXXXX
    5 X     X
    4 X     X
    3 X  F  X
    2 X     X
    1 X     X
    0 XXXXXXX
    --0123456
     */
    @Test
    fun computeBearingCenter() {
        val world = World(7, 7, 0, 0)
        val creature = Creature("C", 100, MovementScoreKeeper())
        val sensor = SmellSensor()
        creature.addComponent(sensor)
        Food(world.getLocation(3, 3)!!, 100)
        world.placeThingAt(creature, 3, 3)
        sensor.computeValue(world, creature)
        logger.info("Strength : ${sensor.getStrength()}")
        assertEquals(0, sensor.getRelativeBearing())
    }

}