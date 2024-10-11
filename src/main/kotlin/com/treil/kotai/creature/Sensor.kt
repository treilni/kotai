package com.treil.kotai.creature

import com.treil.kotai.brain.HasValue
import com.treil.kotai.world.World

/**
 * A part of a creature which can generate a value based on the analysis of its environment.
 * It is connected to a list of inputs and computes an internal value
 * TODO : this looks slightly like a neuron
 * @author Nicolas
 * @since 20/04/2021.
 */
abstract class Sensor {
    // TODO : directly extend HasValue
    /**
     * implementation of HasValue
     * @see HasValue
     */
    data class Value(override var value: Short) : HasValue

    /**
     * A value which can be set on the Short full range and gotten normalized on a smaller scale
     */
    class NormalizedValue(var unnormalizedValue: Short, private val normalizationFactor: Short = 1) : HasValue {
        override val value: Short
            get() = (unnormalizedValue * normalizationFactor).toShort()

    }

    val inputs: MutableList<HasValue> = ArrayList()

    /**
     * Computes the value of this sensor for a creature at it's location
     */
    abstract fun computeValue(world: World, creature: Creature)
}