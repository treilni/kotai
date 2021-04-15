package com.treil.kotai.brain

/**
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