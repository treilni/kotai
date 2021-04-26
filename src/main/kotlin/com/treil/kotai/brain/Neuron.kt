package com.treil.kotai.brain

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Nicolas
 * @since 15/04/2021.
 */

object Constants {
    const val NORMALIZATION_FACTOR = Short.MAX_VALUE.toDouble()
    const val MIN_OUT = Short.MIN_VALUE.toDouble()
    const val OUT_RANGE = Short.MAX_VALUE.toDouble() - Short.MIN_VALUE.toDouble()
}

open class Neuron : HasValue, HasDNA {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(Neuron::class.java.simpleName)
    }

    class Input(private val input: HasValue, var coef: Short, var bias: Short) : HasDNA {
        private val DNA_SIZE = Short.SIZE_BYTES * 2

        fun value(): Int {
            return coef * input.value + bias
        }

        override fun toDNA(): String {
            return coef.toInt().toString() + DNA.Type.BIAS.symbol + bias.toString()
        }

        override fun mutate(mutator: Mutator) {
            if (mutator.getMutationIndex(2, "COEF_OR_BIAS") == 0) {
                coef = mutator.getMutatedShort(coef, DNA.Type.COEF.toString())
            } else {
                bias = mutator.getMutatedShort(bias, DNA.Type.BIAS.toString())
            }
        }

        fun updateCoefFromDNA(dna: String) {
            val split = dna.split(DNA.Type.BIAS.symbol)
            if (split.size != 2) {
                throw IllegalArgumentException("Illegal Input DNA : %s".format(dna))
            }
            coef = split[0].toShort()
            bias = split[1].toShort()
        }

    }

    private val inputs: MutableList<Input> = ArrayList()

    override var value: Short = 0

    fun compute() {
        val fold = inputs.fold(0L) { acc, input -> acc + input.value() }
        val normalized = fold.toDouble() / Constants.NORMALIZATION_FACTOR
        val result = Constants.MIN_OUT + (Constants.OUT_RANGE / (1.0 + Math.exp(-normalized)))
        value = Math.round(result).toShort()
        if (logger.isTraceEnabled) {
            logger.trace("Computed $value for $this")
        }
    }

    fun addInput(value: HasValue, coef: Short = 0, bias: Short = 0) {
        inputs.add(Input(value, coef, bias))
    }

    override fun toDNA(): String {
        return inputs.joinToString(separator = DNA.Type.COEF.symbol.toString()) { input -> input.toDNA() }
    }

    fun setAllCoefs(coef: Short) {
        inputs.forEach { input -> input.coef = coef }
    }

    fun updateCoefsFromDNA(dna: String) {
        val split = dna.ifEmpty() { "0/0" }.split(DNA.Type.COEF.symbol)
        for (i in split.indices) {
            if (i >= inputs.size) {
                break
            }
            inputs[i].updateCoefFromDNA(split[i])
        }
        var i = split.size
        while (i < inputs.size) {
            inputs[i].coef = 0
            i++
        }
    }

    override fun mutate(mutator: Mutator) {
        val i: Int = mutator.getMutationIndex(inputs.size, "INPUT")
        inputs[i].mutate(mutator)
    }

}