package com.treil.kotai.brain

import com.treil.kotai.DNA

/**
 * @author Nicolas
 * @since 17/04/2021.
 */
class Brain(inputLayer: InputLayer, vararg layerSizes: Int) : HasDNA {
    private val layers: MutableList<NeuronLayer>

    constructor(inputLayer: InputLayer, dna: String) : this(inputLayer) {
        val layerDnas = dna.split(DNA.Separator.LAYER.symbol)
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
        return layers.joinToString(DNA.Separator.LAYER.symbol.toString()) { layer -> layer.toDNA() }
    }

    fun compute() {
        layers.forEach { layer -> layer.compute() }
    }
}