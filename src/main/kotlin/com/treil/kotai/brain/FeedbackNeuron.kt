package com.treil.kotai.brain

/**
 * @author Nicolas
 * @since 15/04/2021.
 */
class FeedbackNeuron : Neuron() {
    init {
        addInput(this, 0)
    }
}