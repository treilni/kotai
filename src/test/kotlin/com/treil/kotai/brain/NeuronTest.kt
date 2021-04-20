package com.treil.kotai.brain

import com.treil.kotai.Application
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Nicolas
 * @since 15/04/2021.
 */
internal class NeuronTest {
    companion object {
        @Suppress("unused")
        val logger: Logger = LoggerFactory.getLogger(Application::class.java.simpleName)
    }

    @Test
    fun testSingleOutput() {
        val three: HasValue = StaticValue(3)

        val input: Neuron.Input = Neuron.Input(three, Short.MAX_VALUE, 0)
        assertEquals(3 * Short.MAX_VALUE, input.value())
    }

    @Test
    fun testSingleOutputWithBias() {
        val three: HasValue = StaticValue(3)

        val input: Neuron.Input = Neuron.Input(three, Short.MAX_VALUE, -4)
        assertEquals(3 * Short.MAX_VALUE - 4, input.value())
    }

    @Test
    fun testSingleOutput2() {
        val max: HasValue = StaticValue(Short.MAX_VALUE)

        val input: Neuron.Input = Neuron.Input(max, Short.MAX_VALUE, Short.MAX_VALUE)
        assertEquals(Short.MAX_VALUE * Short.MAX_VALUE + Short.MAX_VALUE, input.value())
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
        val expected: Short = 749
        assertEquals(expected, neuron.value)
    }

    @Test
    fun testComputeMaxOut() {
        val max: HasValue = StaticValue(Short.MAX_VALUE)

        val neuron = Neuron()
        neuron.addInput(max, Short.MAX_VALUE, Short.MAX_VALUE)
        neuron.addInput(max, Short.MAX_VALUE, Short.MAX_VALUE)
        neuron.compute()

        val expected: Short = Short.MAX_VALUE
        assertEquals(expected, neuron.value)
    }

    @Test
    fun testComputeMinOut() {
        val max: HasValue = StaticValue(Short.MAX_VALUE)

        val neuron = Neuron()
        neuron.addInput(max, Short.MIN_VALUE, Short.MIN_VALUE)
        neuron.addInput(max, Short.MIN_VALUE, Short.MIN_VALUE)
        neuron.compute()

        val expected: Short = Short.MIN_VALUE
        assertEquals(expected, neuron.value)
    }

    @Test
    fun testCompute2() {
        val max: HasValue = StaticValue(1)

        val neuron = Neuron()
        neuron.addInput(max, 1000, 3)
        neuron.compute()

        val expected: Short = 1
        assertEquals(expected, neuron.value)
    }
}