package com.treil.kotai.brain

import com.treil.kotai.DNA

/**
 * @author Nicolas
 * @since 15/04/2021.
 */
open class Neuron : HasValue, HasDNA {
    class Input(private val input: HasValue, var coef: Short) : HasDNA {
        private val DNA_SIZE = Short.SIZE_BYTES * 2

        fun value(): Int {
            return coef * input.value
        }

        override fun toDNA(): String {
            val v = coef.toString(16)
            return "0000".substring(v.length) + v
        }

        fun updateCoefFromDNA(remainder: String): String {
            if (remainder.length < DNA_SIZE) {
                coef = 0;
                return "";
            }
            coef = remainder.substring(0, DNA_SIZE).toShort(16)
            return remainder.substring(DNA_SIZE)
        }

    }

    private val inputs: MutableList<Input> = ArrayList()

    override var value: Short = 0

    fun compute() {
        val fold = inputs.fold(0L) { acc, input -> acc + input.value() }
        val v = (fold / Short.MAX_VALUE).toShort()
        if (v > Short.MAX_VALUE) value = Short.MAX_VALUE
        if (v < Short.MIN_VALUE) value = Short.MIN_VALUE
        value = v
    }

    fun addInput(value: HasValue, coef: Short) {
        inputs.add(Input(value, coef))
    }

    override fun toDNA(): String {
        return inputs.fold(StringBuilder()) { b, input -> b.append(input.toDNA()) }.toString()
    }

    fun setAllCoefs(coef: Short) {
        inputs.forEach { input -> input.coef = coef }
    }

    fun updateCoefsFromDNA(dna: String): String {
        var remainder = dna
        for (input in inputs) {
            remainder = input.updateCoefFromDNA(remainder)
        }
        return remainder
    }

}