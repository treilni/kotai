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
            return coef.toInt().toString()
        }

        fun updateCoefFromDNA(dna: String) {
            coef = dna.toShort()
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

    fun addInput(value: HasValue, coef: Short = 0) {
        inputs.add(Input(value, coef))
    }

    override fun toDNA(): String {
        return inputs.joinToString(separator = DNA.Separator.COEF.symbol.toString()) { input -> input.toDNA() }
    }

    fun setAllCoefs(coef: Short) {
        inputs.forEach { input -> input.coef = coef }
    }

    fun updateCoefsFromDNA(dna: String) {
        val split = dna.ifEmpty() { "0" }.split(DNA.Separator.COEF.symbol)
        for (i in split.indices) {
            if (i >= inputs.size) {
                break;
            }
            inputs[i].updateCoefFromDNA(split[i])
        }
        var i = split.size
        while (i < inputs.size) {
            inputs[i].coef = 0
            i++
        }
    }

}