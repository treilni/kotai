package com.treil.kotai.brain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Nicolas
 * @since 17/04/2021.
 */
internal class BrainTest {

    @Test
    fun toDNA() {
        val inputLayer = InputLayer()
        inputLayer.add(NeuronTest.StaticValue(3))
        inputLayer.add(NeuronTest.StaticValue(5))

        val brain = Brain(inputLayer, 2, 3)

        assertEquals("00000000N00000000L00000000N00000000N00000000", brain.toDNA())
    }
}