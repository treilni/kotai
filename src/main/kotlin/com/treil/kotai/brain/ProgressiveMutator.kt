package com.treil.kotai.brain

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.sign
import kotlin.random.Random

/**
 * @author Nicolas
 * @since 19/04/2021.
 */
open class ProgressiveMutator(seed: Int = 0) : Mutator {
    val random = Random(seed)

    companion object {
        val logger: Logger = LoggerFactory.getLogger(ProgressiveMutator::class.java.simpleName)
        const val CHANCE_OF_ADDITIONAL_PCT = 20;
    }

    override fun getMutationIndex(size: Int, reason: String): Int {
        return random.nextInt(0, size)
    }

    override fun getMutationCount(): Int {
        var result = 1
        while (random.nextInt(100) <= CHANCE_OF_ADDITIONAL_PCT) {
            result++
        }
        return result
    }

    override fun getMutatedShort(n: Short, reason: String): Short {
        var i = n.toInt()
        // f(0) = 10, f(32767) = 1000
        val range = 10 + i * i.sign / 64
        i += random.nextInt(-range, range + 1)
        if (i > Short.MAX_VALUE) return Short.MAX_VALUE
        if (i < Short.MIN_VALUE) return Short.MIN_VALUE
        return i.toShort()
    }
}