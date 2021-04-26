package com.treil.kotai.creature

import com.treil.kotai.evolution.ScoreKeeper

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
class Ant(scoreKeeper: ScoreKeeper, initialEnergy: Int = 100) : Creature("Ant", initialEnergy) {
    init {
        addComponent(EyeSensor())
        addComponent(DirectionActuator(scoreKeeper))
        addComponent(MovementActuator(scoreKeeper))
        wireBrain(2)
    }
}