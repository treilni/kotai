package com.treil.kotai.brain

/**
 * A Neuron which uses its output as feedback input. Currently unused
 * @author Nicolas
 * @since 15/04/2021.
 */
class FeedbackNeuron : Neuron() {
    init {
        addInput(this, 0)
    }
}