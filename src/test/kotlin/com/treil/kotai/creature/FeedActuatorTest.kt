package com.treil.kotai.creature

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.treil.kotai.evolution.MovementScoreKeeper
import com.treil.kotai.world.World
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

/**
 * @author Nicolas
 * @since 28/04/2021.
 */
internal class FeedActuatorTest {
    init {
        val root: Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
        root.level = Level.TRACE
    }

    @Test
    fun act() {
        val ant = Ant(MovementScoreKeeper())
        val world = World(3, 3, 0, 0)
        world.placeThingAt(ant, 1, 1)

        ant.liveOneTick(world)
    }
}