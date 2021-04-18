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

        assertEquals("0C0N0C0L0C0N0C0N0C0", brain.toDNA())
    }

    @Test
    fun fromDNA() {
        val inputLayer = InputLayer()
        inputLayer.add(NeuronTest.StaticValue(3))
        inputLayer.add(NeuronTest.StaticValue(5))

        val dna = "1111C2222N3333C4444L5555C6666N7777C8888N9999C8765"
        val brain = Brain(inputLayer, dna)

        assertEquals(dna, brain.toDNA())
    }
}