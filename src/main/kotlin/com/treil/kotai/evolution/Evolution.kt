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
object EvolutionConstants {
    const val KEPT_PERCENT = 20
    const val SAMPLES_PER_DNA = 10
    const val WORLD_SIZE = 40
    const val WORLD_OBSTACLES_PCT = 7
    const val MUTATOR_SEED = 0

    const val CYCLES = 5000
}

fun main(args: Array<String>) {
    val size = args[0].toInt()
    val dnaBank = DNABank(size, Ant(MovementScoreKeeper()))

    val world = World(
        EvolutionConstants.WORLD_SIZE, EvolutionConstants.WORLD_SIZE,
        EvolutionConstants.WORLD_OBSTACLES_PCT
    )
    var lastBest = 0
    for (i in 1..EvolutionConstants.CYCLES) {
        for (dna in dnaBank.getDnas()) {
            val random = Random(0)
            var totalScore = 0
            repeat(EvolutionConstants.SAMPLES_PER_DNA) {
                val scoreKeeper = MovementScoreKeeper()
                val ant = Ant(scoreKeeper, 100)
                ant.getBrain().setDNA(dna)
                world.placeThingAtRandom(ant, random)
                while (!ant.dead) {
                    ant.liveOneTick(world)
                }
                world.removeThing(ant)
                totalScore += scoreKeeper.score
            }
            dnaBank.setScore(dna, totalScore / EvolutionConstants.SAMPLES_PER_DNA)
        }
        val best = dnaBank.getBestDNA()
        if (i % 1000 == 0 || lastBest < best.score) {
            logger.info("Round $i Best DNA scored ${best.score} (generation=${best.generation} dna=${best.dna})")
            lastBest = best.score
        }
        dnaBank.mutate()
    }
}

