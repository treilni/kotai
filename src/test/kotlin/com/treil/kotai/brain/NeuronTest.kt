package com.treil.kotai.brain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Nicolas
 * @since 15/04/2021.
 */
internal class NeuronTest {
    class StaticValue(override val value: Short) : HasValue {
    }

    @Test
    fun testOutput() {
        val three: HasValue = StaticValue(3)

        val input: Neuron.Input = Neuron.Input(three, Short.MAX_VALUE)
        assertEquals(3, input.value())
    }

    @Test
    fun testOutput2() {
        val max: HasValue = StaticValue(Short.MAX_VALUE)

        val input: Neuron.Input = Neuron.Input(max, Short.MAX_VALUE)
        assertEquals(Short.MAX_VALUE.toInt(), input.value())
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
        val two: HasValue = StaticValue(2)
        val three: HasValue = StaticValue(3)

        val neuron = Neuron()
        neuron.addInput(two, 4)
        neuron.addInput(three, -2)
        neuron.compute()
        val expected : Short =  2
        assertEquals(expected, neuron.value)
    }
}