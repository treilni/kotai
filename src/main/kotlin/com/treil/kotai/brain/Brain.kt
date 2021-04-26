package com.treil.kotai.brain

import kotlin.random.Random

/**
 * @author Nicolas
 * @since 17/04/2021.
 */
val random = Random(0)

class Brain(inputLayer: InputLayer, vararg layerSizes: Int) : HasDNA {

    private val layers: MutableList<NeuronLayer>

    constructor(inputLayer: InputLayer, dna: String) : this(inputLayer) {
        val layerDnas = dna.split(DNA.Type.LAYER.symbol)
        var input = inputLayer
        layerDnas.forEach { layerDna ->
            val neuronLayer = NeuronLayer(input)
            neuronLayer.createNeuronsFromDNA(layerDna)
            layers.add(neuronLayer)
            input = neuronLayer
        }
    }

    init {
        layers = ArrayList(layerSizes.size)
        var input = inputLayer
        layerSizes.forEach { size ->
            val element = NeuronLayer(input, size)
            layers.add(element)
            input = element
        }
    }

    override fun toDNA(): String {
        return layers.joinToString(DNA.Type.LAYER.symbol.toString()) { layer -> layer.toDNA() }
    }

    override fun mutate(mutator: Mutator) {
        val layerIndex = mutator.getMutationIndex(layers.size, DNA.Type.LAYER.toString())
        layers[layerIndex].mutate(mutator)
    }

    fun compute() {
        layers.forEach { layer -> layer.compute() }
    }

    fun getSplittedOutput(outputSizes: List<Int>): MutableList<InputLayer> {
        val outLayer = layers[layers.lastIndex]
        val result = ArrayList<InputLayer>()
        var i = 0
        for (outputSize in outputSizes) {
            val inputLayer = InputLayer()
            for (k in 0 until outputSize) {
                val output = outLayer.elements.getOrNull(i++)
                if (output != null) {
                    inputLayer.add(output)
                } else {
                    inputLayer.add(StaticValue(0))
                }
            }
            result.add(inputLayer)
        }
        return result
    }

    fun setDNA(dna: String) {
        val layersDNA = dna.split(DNA.Type.LAYER.symbol)
        for ((index, value) in layersDNA.withIndex()) {
            layers[index].updateCoefsFromDNA(value)
        }
    }

    fun getRandomDNA(): String {
        val dna = toDNA()
        return dna.replace(Regex("-?[0-9]+")) { _ ->
            random.nextInt(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt() + 1).toString()
        }
    }
}