package com.treil.kotai.brain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Nicolas
 * @since 17/04/2021.
 */
internal class BrainTest {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(BrainTest::class.java.simpleName)
    }

    @Test
    fun toDNA() {
        val inputLayer = InputLayer()
        inputLayer.add(StaticValue(3))
        inputLayer.add(StaticValue(5))

        val brain = Brain(inputLayer, 2, 3)

        assertEquals("0/0C0/0N0/0C0/0L0/0C0/0N0/0C0/0N0/0C0/0", brain.toDNA())
    }

    @Test
    fun fromDNA() {
        val inputLayer = InputLayer()
        inputLayer.add(StaticValue(3))
        inputLayer.add(StaticValue(5))

        val dna = "1111/0C2222/0N3333/0C4444/0L5555/0C6666/0N7777/0C8888/0N9999/0C8765/0"
        val brain = Brain(inputLayer, dna)

        assertEquals(dna, brain.toDNA())
    }

    @Test
    fun mutate() {
        val inputLayer = InputLayer()
        inputLayer.add(StaticValue(0))
        inputLayer.add(StaticValue(0))

        val mutator = TestMutator()
        val brain = Brain(inputLayer, dna = "0/0C0/0N0/0C0/0L0/0C0/0N0/0C0/0N0/0C0/0")
        brain.mutate(mutator)
        logger.info("-----------------")
        assertEquals("0/0C0/0N0/10C0/0L0/0C0/0N0/0C0/0N0/0C0/0", brain.toDNA())
        brain.mutate(mutator)
        logger.info("-----------------")
        assertEquals("0/0C0/0N0/10C0/0L0/0C-5/0N0/0C0/0N0/0C0/0", brain.toDNA())
    }

    @Test
    internal fun randomDNA() {
        val inputLayer = InputLayer()
        inputLayer.add(StaticValue(0))
        inputLayer.add(StaticValue(0))
        val brain = Brain(inputLayer, 2, 3)
        logger.info(brain.getRandomDNA())
    }
}