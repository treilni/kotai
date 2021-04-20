package com.treil.kotai.creature

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
class Ant : Creature(100) {
    init {
        addComponent(EyeSensor())
        addComponent(DirectionalMovementActuator())
    }
}