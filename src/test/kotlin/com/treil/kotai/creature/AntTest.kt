package com.treil.kotai.creature

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.treil.kotai.evolution.MovementScoreKeeper
import com.treil.kotai.world.World
import org.slf4j.LoggerFactory
import kotlin.test.Test

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
internal class AntTest {
    init {
        val root: Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
        root.level = Level.DEBUG
    }

    companion object {
        val logger: org.slf4j.Logger = LoggerFactory.getLogger(AntTest::class.java.simpleName)
    }

    @Test
    fun liveStatic() {
        val world = World(20, 20)
        val ant = Ant(MovementScoreKeeper(), 10)

        world.placeThingAt(ant, 5, 5)
        while (!ant.dead) {
            ant.liveOneTick(world)
        }
    }


    @Test
    fun liveDynamic() {
        val world = World(10, 10)
        val ant = Ant(MovementScoreKeeper(), 10)
        ant.wireBrain(2)
        logger.info("DNA = ${ant.getBrain().toDNA()}")
        ant.getBrain().setDNA("0/0N0/0L0/0N0/32000")
        logger.info("DNA = ${ant.getBrain().toDNA()}")

        world.placeThingAt(ant, 5, 5)
        while (!ant.dead) {
            ant.liveOneTick(world)
        }
    }
}