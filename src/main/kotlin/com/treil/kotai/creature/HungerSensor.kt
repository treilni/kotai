package com.treil.kotai.creature

import com.treil.kotai.world.World
import java.lang.Integer.max

/**
 * A sensor which outputs a value between Short.MIN_VALUE (empty) and Short.MAX_VALUE (full) depending on a creature's energy bar
 */
class HungerSensor : Sensor() {
    private val hungerValue = Value(0)

    init {
        inputs.add(hungerValue)
    }

    /**
     * f(x) = a * x + b
     * f(0) = b = Short.MIN_VALUE
     * f(maxEnergy) = a * maxEnergy + Short.MIN_VALUE = Short.MAX_VALUE
     */
    override fun computeValue(world: World, creature: Creature) {
        val maxEnergy = max(creature.getMaxEnergy(), 1)
        val intValue =
            Short.MIN_VALUE + (Short.MAX_VALUE - Short.MIN_VALUE) * (maxEnergy - creature.getEnergy()) / maxEnergy
        hungerValue.value = intValue.toShort()
    }
}
