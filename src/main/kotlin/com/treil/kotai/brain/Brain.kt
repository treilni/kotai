package com.treil.kotai.brain

import com.treil.kotai.DNA

/**
 * @author Nicolas
 * @since 17/04/2021.
 */
class Brain(val inputLayer: InputLayer, vararg layerSizes: Int) : HasDNA {
    val layers: MutableList<NeuronLayer>

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

}