package com.treil.kotai.brain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Nicolas
 * @since 15/04/2021.
 */
internal class NeuronTest {
    class StaticValue(override val value: Short) : HasValue

    @Test
    fun testOutput() {
        val three: HasValue = StaticValue(3)

        val input: Neuron.Input = Neuron.Input(three, Short.MAX_VALUE)
        assertEquals(3 * Short.MAX_VALUE, input.value())
    }

    @Test
    fun testOutput2() {
        val max: HasValue = StaticValue(Short.MAX_VALUE)

        val input: Neuron.Input = Neuron.Input(max, Short.MAX_VALUE)
        assertEquals(Short.MAX_VALUE * Short.MAX_VALUE, input.value())
    }

    @Test
    fun testMax() {
        val max: HasValue = StaticValue(Short.MAX_VALUE)

        val neuron = Neuron()
        neuron.addInput(max, Short.MAX_VALUE)
        neuron.compute()
        assertEquals(Short.MAX_VALUE, neuron.value)
    }

    @Test
    fun testCompute() {
        val twenty: HasValue = StaticValue(20)
        val ten: HasValue = StaticValue(10)
        val half = (Short.MAX_VALUE / 2).toShort()

        val neuron = Neuron()
        neuron.addInput(twenty, Short.MAX_VALUE)
        neuron.addInput(ten, (-half).toShort())
        neuron.compute()
        val expected: Short = 15
        assertEquals(expected, neuron.value)
    }
}