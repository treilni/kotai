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
        assertEquals("0/0C0/0", neuron.toDNA())
        layer.add(Neuron())
        layer.add(Neuron())
        assertEquals("0/0C0/0N0/0C0/0N0/0C0/0", layer.toDNA())

        neuron.setAllCoefs(Short.MAX_VALUE)
        assertEquals("32767/0C32767/0N0/0C0/0N0/0C0/0", layer.toDNA())
    }

    @Test
    fun updateCoefsFromDNA() {
        val inputLayer = buildInputLayer()

        val layer = NeuronLayer(inputLayer)
        layer.add(Neuron())
        layer.add(Neuron())
        assertEquals("0/0C0/0N0/0C0/0", layer.toDNA())

        layer.updateCoefsFromDNA("1247/0C-34/0N-31755/0C0/0")
        assertEquals("1247/0C-34/0N-31755/0C0/0", layer.toDNA())
    }

    @Test
    fun updateCoefsFromDNAWithSpuriousCoef() {
        val inputLayer = buildInputLayer()

        val layer = NeuronLayer(inputLayer)
        layer.add(Neuron())
        layer.add(Neuron())
        layer.updateCoefsFromDNA("1247/0C-34/0N-31755/0C0/0N0/0C0/0")
        assertEquals("1247/0C-34/0N-31755/0C0/0", layer.toDNA())
    }

    @Test
    fun updateCoefsFromDNAWithMissingCoef() {
        val inputLayer = buildInputLayer()

        val layer = NeuronLayer(inputLayer)
        layer.add(Neuron())
        layer.add(Neuron())
        layer.updateCoefsFromDNA("1247/0C-34/0N")
        assertEquals("1247/0C-34/0N0/0C0/0", layer.toDNA())

        layer.updateCoefsFromDNA("1247/0N-31755/0C0/0")
        assertEquals("1247/0C0/0N-31755/0C0/0", layer.toDNA())
    }

    @Test
    fun updateCoefsFromDNAWithBias() {
        val inputLayer = buildInputLayer()

        val layer = NeuronLayer(inputLayer)
        layer.add(Neuron())
        layer.add(Neuron())

        layer.updateCoefsFromDNA("1247/1C-34/2N-31755/-5C0/8")
        assertEquals("1247/1C-34/2N-31755/-5C0/8", layer.toDNA())
    }

    private fun buildInputLayer(): InputLayer {
        val inputLayer = InputLayer()
        inputLayer.add(NeuronTest.StaticValue(4))
        inputLayer.add(NeuronTest.StaticValue(6))
        return inputLayer
    }
}