package com.treil.kotai.brain

/**
 * DNA is a string representing layers of lists of Neuron information
 * @author Nicolas
 * @since 15/04/2021.
 */
object DNA {
    fun getLayerSizes(dna: String): List<Int> {
        val layersDna = dna.split(Type.LAYER.symbol)
        val result = ArrayList<Int>()
        for (layerDna in layersDna) {
            result.add(layerDna.split(Type.NEURON.symbol).size)
        }
        return result
    }

    enum class Type(val symbol: Char) {
        LAYER('L'), NEURON('N'), COEF('C'), BIAS('/');
    }
}