package com.treil.kotai.creature

import com.treil.kotai.brain.HasValue
import com.treil.kotai.world.World

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
abstract class Sensor {
    data class Value(override var value: Short) : HasValue

    class NormalizedValue(var unnormalizedValue: Short, private val normalizationFactor: Short = 1) : HasValue {
        override val value: Short
            get() = (unnormalizedValue * normalizationFactor).toShort()

    }

    val inputs: MutableList<HasValue> = ArrayList()

    abstract fun computeValue(world: World, creature: Creature)
}