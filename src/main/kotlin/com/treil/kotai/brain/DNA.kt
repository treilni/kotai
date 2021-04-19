package com.treil.kotai.brain

/**
 * @author Nicolas
 * @since 15/04/2021.
 */
class DNA {
    enum class Type(val symbol: Char) {
        LAYER('L'), NEURON('N'), COEF('C'), BIAS('/');
    }
}