package com.treil.kotai.brain

import com.treil.kotai.DNA

/**
 * @author Nicolas
 * @since 15/04/2021.
 */
class NeuronLayer(private val inputLayer: InputLayer) : InputLayer(), HasDNA {
    constructor(inputLayer: InputLayer, size: Int) : this(inputLayer) {
        repeat(size) { i -> add(Neuron()) }
    }

    private val neurons: MutableList<Neuron> = ArrayList()

    override fun compute() {
        inputLayer.compute()
        neurons.forEach { neuron -> neuron.compute() }
    }

    fun add(neuron: Neuron) {
        super.add(neuron)
        inputLayer.elements.forEach { input -> neuron.addInput(input, 0) }
        neurons.add(neuron)
    }

    override fun toDNA(): String {
        return neurons.joinToString(separator = DNA.Separator.NEURON.symbol.toString()) { neuron -> neuron.toDNA() }
    }

    fun updateCoefsFromDNA(dna: String) {
        val split = ArrayList(dna.split(DNA.Separator.NEURON.symbol))
        for ((i, neuron) in neurons.withIndex()) {
            val neuronDna = split.getOrElse(i) { "" }
            neuron.updateCoefsFromDNA(neuronDna)
        }
    }

    fun mutate() {

    }

    fun createNeuronsFromDNA(dna: String) {
        val split = ArrayList(dna.split(DNA.Separator.NEURON.symbol))
        repeat(split.size) { i ->
            val neuron = Neuron()
            add(neuron)
            neuron.updateCoefsFromDNA(split[i])
        }
    }
}