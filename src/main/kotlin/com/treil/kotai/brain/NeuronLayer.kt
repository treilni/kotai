package com.treil.kotai.brain

import com.treil.kotai.DNA
import kotlin.streams.toList

/**
 * @author Nicolas
 * @since 15/04/2021.
 */
class NeuronLayer(private val inputLayer: InputLayer) : InputLayer(), HasDNA {
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
        val symbol = DNA.Type.NEURON.symbol.toString()
        return neurons.fold(StringBuilder()) { acc, neuron ->
            acc.append(symbol)
            acc.append(neuron.toDNA())
        }
            .toString()
    }

    fun updateCoefsFromDNA(dna: String) {
        val list = dna.split(DNA.Type.NEURON.symbol).stream().filter { s -> s.isNotEmpty() }.toList();
        val split = ArrayList(list)
        while (split.size < neurons.size) {
            split.add("")
        }
        for ((i, neuron) in neurons.withIndex()) {
            neuron.updateCoefsFromDNA(split[i])
        }
    }

    fun mutate() {

    }
}