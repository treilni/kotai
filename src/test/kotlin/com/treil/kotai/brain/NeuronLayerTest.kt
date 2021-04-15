package com.treil.kotai.brain

import com.treil.kotai.Application
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

/**
 * @author Nicolas
 * @since 15/04/2021.
 */
internal class NeuronLayerTest {
    companion object {
        val logger = LoggerFactory.getLogger(Application::class.java.getSimpleName())
    }

    @Test
    fun testDNA() {
        val inputLayer = buildInputLayer()

        val layer = NeuronLayer(inputLayer)
        val neuron = Neuron()

        layer.add(neuron)
        assertEquals("00000000", neuron.toDNA())
        layer.add(Neuron())
        layer.add(Neuron())
        assertEquals("N00000000N00000000N00000000", layer.toDNA())

        neuron.setAllCoefs(Short.MAX_VALUE)
        assertEquals("N7fff7fffN00000000N00000000", layer.toDNA())
    }

    @Test
    fun updateCoefsFromDNA() {
        val inputLayer = buildInputLayer()

        val layer = NeuronLayer(inputLayer)
        layer.add(Neuron())
        layer.add(Neuron())
        assertEquals("N00000000N00000000", layer.toDNA())

        layer.updateCoefsFromDNA("N07ff3abcN12345678")
        assertEquals("N07ff3abcN12345678", layer.toDNA())

        layer.updateCoefsFromDNA("N07ff3abcN12345678N00000000")
        assertEquals("N07ff3abcN12345678", layer.toDNA())

        layer.updateCoefsFromDNA("N07ff3abcN12345678N00000000")
        assertEquals("N07ff3abcN12345678", layer.toDNA())

        layer.updateCoefsFromDNA("N07ff3abcN")
        assertEquals("N07ff3abcN00000000", layer.toDNA())

        layer.updateCoefsFromDNA("N1234abcN07ff3abc")
        assertEquals("N12340000N07ff3abc", layer.toDNA())
    }

    private fun buildInputLayer(): InputLayer {
        val inputLayer = InputLayer()
        inputLayer.add(NeuronTest.StaticValue(4))
        inputLayer.add(NeuronTest.StaticValue(6))
        return inputLayer
    }
}