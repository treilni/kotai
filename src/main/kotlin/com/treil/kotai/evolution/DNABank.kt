package com.treil.kotai.evolution

import com.treil.kotai.brain.Mutator
import com.treil.kotai.brain.ProgressiveMutator
import com.treil.kotai.brain.RandomMutator
import com.treil.kotai.creature.Ant
import com.treil.kotai.creature.Creature
import com.treil.kotai.world.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.random.Random

/**
 * @author Nicolas
 * @since 23/04/2021.
 *
 * -7/-1213N-12707/-29040L-16235/-24260C717/-22492N16654/17873C-14970/12566 (1201)
 */
data class DNAScore(val dna: String, var score: Int = 0, var generation: Int = 1)

object EvolutionConstants {
    const val KEPT_PERCENT = 20
    const val SAMPLES_PER_DNA = 10
    const val WORLD_SIZE = 15
    const val MUTATOR_SEED = 0

    const val CYCLES = 25000
}

val logger: Logger = LoggerFactory.getLogger(DNABank::class.java.simpleName)

class DNABank(val size: Int, val sampleCreature: Creature) {

    private val dnaScores: MutableList<DNAScore> = ArrayList()
    private val fromDna: MutableMap<String, DNAScore> = HashMap()

    private val randomMutator = RandomMutator(EvolutionConstants.MUTATOR_SEED)
    private val progressiveMutator = ProgressiveMutator(EvolutionConstants.MUTATOR_SEED)

    init {
        repeat(size) {
            val element = DNAScore(sampleCreature.getBrain().getRandomDNA(), 0)
            fromDna[element.dna] = element
            dnaScores.add(element)
        }
    }

    fun setScore(dna: String, score: Int) {
        val scoredDNA = fromDna[dna]
        if (scoredDNA != null) {
            scoredDNA.score = score
        } else {
            logger.error("DNA not found in bank : $dna")
        }
    }

    fun mutate() {
        // sort by score
        dnaScores.sortBy { dnaScore: DNAScore ->
            -dnaScore.score
        }

        // Keep x percent
        val kept = size * EvolutionConstants.KEPT_PERCENT / 100
        val newBank = ArrayList<DNAScore>()
        for (i in 0 until kept) {
            newBank.add(dnaScores[i])
            val (scoredDNA, mutatedDna) = getMutatedDNA(i, progressiveMutator)
            newBank.add(DNAScore(mutatedDna, 0, scoredDNA.generation + 1))
        }
        var i = 0
        while (newBank.size < dnaScores.size) {
            val (scoredDNA, mutatedDna) = getMutatedDNA(i++, randomMutator)
            newBank.add(DNAScore(mutatedDna, 0, scoredDNA.generation + 1))
        }

        // update accessMap
        dnaScores.clear()
        dnaScores.addAll(newBank)
        fromDna.clear()
        for (dnaScore in dnaScores) {
            fromDna[dnaScore.dna] = dnaScore
        }
    }

    private fun getMutatedDNA(i: Int, mutator: Mutator): Pair<DNAScore, String> {
        val brain = sampleCreature.getBrain()
        val scoredDNA = dnaScores[i]
        brain.setDNA(scoredDNA.dna)
        brain.mutate(mutator)
        val mutatedDna = brain.toDNA()
        return Pair(scoredDNA, mutatedDna)
    }

    fun getDnas(): Set<String> {
        return fromDna.keys
    }

    fun getBestDNA(): DNAScore {
        val sorted = dnaScores.sortedBy { dnaScore: DNAScore ->
            -dnaScore.score
        }
        return sorted[0]
    }
}


fun main(args: Array<String>) {
    val size = args[0].toInt()
    val dnaBank = DNABank(size, Ant(MovementScoreKeeper()))

    val world = World(EvolutionConstants.WORLD_SIZE, EvolutionConstants.WORLD_SIZE, 0)
    var lastBest = 0
    for (i in 1..EvolutionConstants.CYCLES) {
        for (dna in dnaBank.getDnas()) {
            val random = Random(0)
            var totalScore = 0
            repeat(EvolutionConstants.SAMPLES_PER_DNA) {
                val scoreKeeper = MovementScoreKeeper()
                val ant = Ant(scoreKeeper, 50)
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

