package com.treil.kotai.evolution

import com.treil.kotai.brain.Mutator
import com.treil.kotai.brain.ProgressiveMutator
import com.treil.kotai.brain.RandomMutator
import com.treil.kotai.creature.Creature
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Nicolas
 * @since 23/04/2021.
 *
 * -7/-1213N-12707/-29040L-16235/-24260C717/-22492N16654/17873C-14970/12566 (1201)
 */
data class DNAScore(val dna: String, var score: Int = 0, var generation: Int = 1)

val logger: Logger = LoggerFactory.getLogger(DNABank::class.java.simpleName)

class DNABank(val size: Int, val sampleCreature: Creature) {

    private val dnaScores: MutableList<DNAScore> = ArrayList()
    private val fromDna: MutableMap<String, DNAScore> = HashMap()

    private val randomMutator = RandomMutator(Evolution.MUTATOR_SEED)
    private val progressiveMutator = ProgressiveMutator(Evolution.MUTATOR_SEED)

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
        val kept = size * Evolution.KEPT_PERCENT / 100
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

