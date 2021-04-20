package com.treil.kotai.creature

import com.treil.kotai.world.World

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
abstract class Actuator {
    abstract fun act(world: World, creature: Creature)
}