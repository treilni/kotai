package com.treil.kotai.creature

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.treil.kotai.evolution.MovementScoreKeeper
import com.treil.kotai.evolution.logger
import com.treil.kotai.world.World
import org.slf4j.LoggerFactory
import kotlin.random.Random

/**
 * @author Nicolas
 * @since 26/04/2021.
 */
fun main() {
    val root: Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    root.level = Level.DEBUG
    val random = Random(0)

    val world = World(20, 20)
    val dna = "-7/-1213N-12707/-29040L-16235/-24260C717/-22492N16654/17873C-14970/12566"
    val scoreKeeper = MovementScoreKeeper()
    val ant = Ant(scoreKeeper, 50)
    ant.getBrain().setDNA(dna)
    world.placeThingAtRandom(ant, random)
    while (!ant.dead) {
        ant.liveOneTick(world)
    }
    logger.info("Final score : ${scoreKeeper.score}")
    world.removeThing(ant)
}