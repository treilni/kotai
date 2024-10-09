package com.treil.kotai.brain

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * A representation of a neuron. A neuron has inputs which are a set of values, and a single output value which can be computed.
 * Computation is using a coeficient and bias for each input value
 * Coefficients and biases can be parametrized by reading a DNA chain
 * @author Nicolas
 * @since 15/04/2021.
 */

object Constants {
    const val NORMALIZATION_FACTOR = Short.MAX_VALUE.toDouble() * 50.0
    const val MIN_OUT = Short.MIN_VALUE.toDouble()
    const val OUT_RANGE = Short.MAX_VALUE.toDouble() - Short.MIN_VALUE.toDouble()
}

open class Neuron : HasValue, HasDNA {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(Neuron::class.java.simpleName)
    }

    /**
     * An input connexion to a neuron (i.e. an entry). An input has a coef and bias to compute the contribution of this connexion to
     * the neuron's output
     */
    class InputConnexion(private val input: HasValue, var coef: Short, var bias: Short) : HasDNA {
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

    private val inputConnexions: MutableList<InputConnexion> = ArrayList()

    override var value: Short = 0

    fun compute() {
        val fold = inputConnexions.fold(0L) { acc, input -> acc + input.value() }
        val normalized = fold.toDouble() / Constants.NORMALIZATION_FACTOR
        val result = Constants.MIN_OUT + (Constants.OUT_RANGE / (1.0 + Math.exp(-normalized)))
        value = Math.round(result).toShort()
        if (logger.isTraceEnabled) {
            logger.trace("Computed $value for $this")
        }
    }

    fun addInput(value: HasValue, coef: Short = 0, bias: Short = 0) {
        inputConnexions.add(InputConnexion(value, coef, bias))
    }

    /**
     * Used to generate the DNA chain corresponding to this neuron's connexion parameters
     */
    override fun toDNA(): String {
        return inputConnexions.joinToString(separator = DNA.Type.COEF.symbol.toString()) { input -> input.toDNA() }
    }

    fun setAllCoefs(coef: Short) {
        inputConnexions.forEach { input -> input.coef = coef }
    }

    /**
     * Used to read a DNA chain to set neuron connexion parameters
     */
    fun updateCoefsFromDNA(dna: String) {
        val split = dna.ifEmpty() { "0/0" }.split(DNA.Type.COEF.symbol)
        for (i in split.indices) {
            if (i >= inputConnexions.size) {
                break
            }
            inputConnexions[i].updateCoefFromDNA(split[i])
        }
        var i = split.size
        while (i < inputConnexions.size) {
            inputConnexions[i].coef = 0
            i++
        }
    }

    /**
     * A function to mutate one of this neuron's input
     */
    override fun mutate(mutator: Mutator) {
        val i: Int = mutator.getMutationIndex(inputConnexions.size, "INPUT")
        inputConnexions[i].mutate(mutator)
    }

}