package com.treil.kotai.creature

import com.treil.kotai.brain.InputLayer
import com.treil.kotai.evolution.ScoreKeeper
import com.treil.kotai.world.World

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
abstract class Actuator(val scoreKeeper: ScoreKeeper, val inputSize: Int = 1) {
    var inputLayer: InputLayer? = null

    abstract fun act(world: World, creature: Creature)
}