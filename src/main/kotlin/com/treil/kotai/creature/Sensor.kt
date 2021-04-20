package com.treil.kotai.creature

import com.treil.kotai.brain.HasValue
import com.treil.kotai.world.World

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
abstract class Sensor {
    val inputs: MutableList<HasValue> = ArrayList()

    abstract fun computeValue(world: World, creature: Creature)
}