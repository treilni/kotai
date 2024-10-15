package com.treil.kotai.evolution

import com.treil.kotai.creature.Ant
import com.treil.kotai.evolution.Evolution.MAX_UNSUCCESSFUL_CYCLES
import com.treil.kotai.world.World
import kotlin.random.Random

/**
 * @author Nicolas
 * @since 23/04/2021.
 *
 * -7/-1213N-12707/-29040L-16235/-24260C717/-22492N16654/17873C-14970/12566 (1201)
 */
object Evolution {
    const val MUTATOR_SEED = 100

    const val KEPT_PERCENT = 25
    const val SAMPLES_PER_DNA = 1
    const val MAX_CYCLES = 200000
    const val MAX_UNSUCCESSFUL_CYCLES = 20000

    const val WORLD_SIZE = 100
    const val WORLD_OBSTACLES_PCT = 6
    const val WORLD_FOOD_PM = 15
    const val INITIAL_ENERGY = 50


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

    var lastBest = 0
    var unsuccessfulCycles = 0
    for (i in 1..Evolution.MAX_CYCLES) {
        if (unsuccessfulCycles >= MAX_UNSUCCESSFUL_CYCLES) {
            logger.info("Stopped evolving after $unsuccessfulCycles unsuccessful cycles.")
            break
        }
        if (i % 100 == 0) {
            logger.info("CYCLE $i, best score : ${lastBest}, unsuccessful cycles : $unsuccessfulCycles.")
        }
        val world = World(
            Evolution.WORLD_SIZE, Evolution.WORLD_SIZE,
            Evolution.WORLD_OBSTACLES_PCT,
            Evolution.WORLD_FOOD_PM
        )
        for (dna in dnaBank.getDnas()) {
            val random = Random(0)
            var totalScore = 0
            repeat(Evolution.SAMPLES_PER_DNA) {
                val scoreKeeper = MovementScoreKeeper()
                val ant = Ant(scoreKeeper, "Ant", Evolution.INITIAL_ENERGY)
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
        if (lastBest < best.score) {
            logger.info("Round $i Best DNA scored ${best.score} (generation=${best.generation} dna=${best.dna})")
            System.err.println("val dna = \"${best.dna}\"")
            lastBest = best.score
            unsuccessfulCycles = 0
        }
        else {
            unsuccessfulCycles++
        }
        dnaBank.mutate()
    }
}

