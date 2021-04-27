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

    val world = World(15, 15)
    //val dna = "-7/-1213N-12707/-29040L-16235/-24260C717/-22492N16654/17873C-14970/12566"
    //val dna = "-3/-3347N13663/-2960L-16235/-6888C18328/-13889N-26978/10204C-17016/-18089"
    val dna = "-875/-5662N-25029/-16577L-3537/12957C1226/-22290N17201/-19438C-21538/10133"

    val scoreKeeper = MovementScoreKeeper()
    val ant = Ant(scoreKeeper, 100)
    ant.getBrain().setDNA(dna)
    world.placeThingAtRandom(ant, random)
    while (!ant.dead) {
        ant.liveOneTick(world)
    }
    logger.info("Final score : ${scoreKeeper.score}")
    world.removeThing(ant)
}