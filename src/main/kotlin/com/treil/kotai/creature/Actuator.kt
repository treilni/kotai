package com.treil.kotai.creature

import com.treil.kotai.brain.InputLayer
import com.treil.kotai.evolution.ScoreKeeper
import com.treil.kotai.world.World

/**
 * A part of a creature which can alter its state (position, energy etc.)
 * An actuator responds to an input signal consisting in 'n' input values (1 by default)
 * @author Nicolas
 * @since 20/04/2021.
 */
abstract class Actuator(val scoreKeeper: ScoreKeeper, val inputSize: Int = 1) {
    var inputLayer: InputLayer? = null

    abstract fun act(world: World, creature: Creature)
}