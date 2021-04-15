package com.treil.kotai.brain

/**
 * @author Nicolas
 * @since 15/04/2021.
 */
class Neuron : HasValue {
    class Input(val input: HasValue, var coef: Short) {

        fun value(): Int {
            return coef * input.value
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
}