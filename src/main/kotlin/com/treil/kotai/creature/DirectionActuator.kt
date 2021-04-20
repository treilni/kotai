package com.treil.kotai.creature

import com.treil.kotai.world.Direction
import com.treil.kotai.world.World

class DirectionActuator : Actuator() {
    override fun act(world: World, creature: Creature) {
        val hasValue = inputLayer?.elements?.get(0)
        if (hasValue != null) {
            val v = hasValue.value.toInt()
            val deltaDegrees = 180 * v / Short.MAX_VALUE
            val newDirection = Direction(creature.facing)
            newDirection.add(deltaDegrees.toShort())
            creature.facing = newDirection
        }
    }
}
