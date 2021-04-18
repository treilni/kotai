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
internal class NeuronLayerTest {
    @Suppress("unused")
    companion object {
        val logger: Logger = LoggerFactory.getLogger(Application::class.java.simpleName)
    }

    @Test
    fun testDNA() {
        val inputLayer = buildInputLayer()

        val layer = NeuronLayer(inputLayer)
        val neuron = Neuron()

        layer.add(neuron)
        assertEquals("0C0", neuron.toDNA())
        layer.add(Neuron())
        layer.add(Neuron())
        assertEquals("0C0N0C0N0C0", layer.toDNA())

        neuron.setAllCoefs(Short.MAX_VALUE)
        assertEquals("32767C32767N0C0N0C0", layer.toDNA())
    }

    @Test
    fun updateCoefsFromDNA() {
        val inputLayer = buildInputLayer()

        val layer = NeuronLayer(inputLayer)
        layer.add(Neuron())
        layer.add(Neuron())
        assertEquals("0C0N0C0", layer.toDNA())

        layer.updateCoefsFromDNA("1247C-34N-31755C0")
        assertEquals("1247C-34N-31755C0", layer.toDNA())

        layer.updateCoefsFromDNA("1247C-34N-31755C0N0C0")
        assertEquals("1247C-34N-31755C0", layer.toDNA())

        layer.updateCoefsFromDNA("1247C-34N")
        assertEquals("1247C-34N0C0", layer.toDNA())

        layer.updateCoefsFromDNA("1247N-31755C0")
        assertEquals("1247C0N-31755C0", layer.toDNA())
    }

    private fun buildInputLayer(): InputLayer {
        val inputLayer = InputLayer()
        inputLayer.add(NeuronTest.StaticValue(4))
        inputLayer.add(NeuronTest.StaticValue(6))
        return inputLayer
    }
}