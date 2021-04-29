package com.treil.kotai.evolution

import com.treil.kotai.creature.Ant
import com.treil.kotai.world.World
import kotlin.random.Random

/**
 * @author Nicolas
 * @since 23/04/2021.
 *
 * -7/-1213N-12707/-29040L-16235/-24260C717/-22492N16654/17873C-14970/12566 (1201)
 */
object Evolution {
    const val MUTATOR_SEED = 0

    const val KEPT_PERCENT = 20
    const val SAMPLES_PER_DNA = 5
    const val CYCLES = 25000

    const val WORLD_SIZE = 20
    const val WORLD_OBSTACLES_PCT = 7
    const val WORLD_FOOD_PM = 50
    const val INITIAL_ENERGY = 200


    fun createWorld(): World {
        return World(
            WORLD_SIZE, WORLD_SIZE,
            WORLD_OBSTACLES_PCT,
            WORLD_FOOD_PM
        )
    }
}

fun main(args: Array<String>) {
    val size = args[0].toInt()
    val dnaBank = DNABank(size, Ant(MovementScoreKeeper()))

    val world = World(
        Evolution.WORLD_SIZE, Evolution.WORLD_SIZE,
        Evolution.WORLD_OBSTACLES_PCT
    )
    var lastBest = 0
    for (i in 1..Evolution.CYCLES) {
        for (dna in dnaBank.getDnas()) {
            val random = Random(0)
            var totalScore = 0
            repeat(Evolution.SAMPLES_PER_DNA) {
                val scoreKeeper = MovementScoreKeeper()
                val ant = Ant(scoreKeeper, Evolution.INITIAL_ENERGY)
                ant.getBrain().setDNA(dna)
                world.placeThingAtRandom(ant, random)
                while (!ant.dead) {
                    ant.liveOneTick(world)
                }
                world.removeThing(ant)
                totalScore += scoreKeeper.score
            }
            dnaBank.setScore(dna, totalScore / Evolution.SAMPLES_PER_DNA)
        }
        val best = dnaBank.getBestDNA()
        if (i % 1000 == 0 || lastBest < best.score) {
            logger.info("Round $i Best DNA scored ${best.score} (generation=${best.generation} dna=${best.dna})")
            lastBest = best.score
        }
        dnaBank.mutate()
    }
}

