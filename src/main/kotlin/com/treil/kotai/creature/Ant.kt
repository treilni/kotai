package com.treil.kotai.creature

import com.treil.kotai.evolution.ScoreKeeper

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
class Ant(scoreKeeper: ScoreKeeper, name: String = "Ant", initialEnergy: Int = 100) :
    Creature(name, initialEnergy, scoreKeeper) {
    constructor(scoreKeeper: ScoreKeeper, initialEnergy: Int) : this(scoreKeeper, "Ant", initialEnergy)

    init {
        addComponent(EyeSensor())
        addComponent(LocalSensor())
        addComponent(SmellSensor())
        addComponent(HungerSensor())

        addComponent(FeedActuator(scoreKeeper)) // feed before moving :)
        addComponent(DirectionActuator(scoreKeeper))
        addComponent(MovementActuator(scoreKeeper))
        wireBrain(5, 5)
    }
}