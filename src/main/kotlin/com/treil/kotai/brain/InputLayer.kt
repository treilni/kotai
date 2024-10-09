package com.treil.kotai.brain

/**
 * A collection of values which can come from an array of sensors or a layer of neurons
 * @author Nicolas
 * @since 15/04/2021.
 */
open class InputLayer {
    val elements: MutableList<HasValue> = ArrayList()

    fun add(hasValue: HasValue) {
        elements.add(hasValue)
    }

    open fun compute() {}
}