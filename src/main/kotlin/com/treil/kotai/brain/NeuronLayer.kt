package com.treil.kotai.brain

/**
 * @author Nicolas
 * @since 15/04/2021.
 */
class NeuronLayer(private val inputLayer: InputLayer) : InputLayer(), HasDNA {
    constructor(inputLayer: InputLayer, size: Int) : this(inputLayer) {
        repeat(size) { add(Neuron()) }
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
        return neurons.joinToString(separator = DNA.Type.NEURON.symbol.toString()) { neuron -> neuron.toDNA() }
    }

    override fun mutate(mutator: Mutator) {
        val neuronIndex = mutator.getMutationIndex(neurons.size, DNA.Type.NEURON.toString())
        neurons[neuronIndex].mutate(mutator)
    }

    fun updateCoefsFromDNA(dna: String) {
        val split = ArrayList(dna.split(DNA.Type.NEURON.symbol))
        for ((i, neuron) in neurons.withIndex()) {
            val neuronDna = split.getOrElse(i) { "" }
            neuron.updateCoefsFromDNA(neuronDna)
        }
    }

    fun createNeuronsFromDNA(dna: String) {
        val split = ArrayList(dna.split(DNA.Type.NEURON.symbol))
        repeat(split.size) { i ->
            val neuron = Neuron()
            add(neuron)
            neuron.updateCoefsFromDNA(split[i])
        }
    }
}