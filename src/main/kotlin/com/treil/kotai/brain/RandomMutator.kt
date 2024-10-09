package com.treil.kotai.brain

/**
 * A Mutator which applies perfectly random mutation to a short
 * @author Nicolas
 * @since 19/04/2021.
 */
class RandomMutator(seed: Int = 0) : ProgressiveMutator(seed) {
    override fun getMutatedShort(n: Short, reason: String): Short {
        return random.nextInt(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
    }
}