package com.treil.kotai.brain

/**
 * An interface to a mutator object which can compute DNA mutations
 */
interface Mutator {
    /**
     * Provides a value between 0 and size - 1 to target where the mutation will be applied
     */
    fun getMutationIndex(size: Int, reason: String): Int

    /**
     * Provides the number of mutations to apply
     */
    fun getMutationCount(): Int

    /**
     * Provides a mutated version of a short value
     */
    fun getMutatedShort(n: Short, reason: String): Short
}
