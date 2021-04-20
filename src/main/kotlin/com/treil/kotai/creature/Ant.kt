package com.treil.kotai.creature

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
class Ant : Creature("Ant", 100) {
    init {
        addComponent(EyeSensor())
        addComponent(DirectionActuator())
        addComponent(MovementActuator())
    }
}