package com.treil.kotai.world

import kotlin.math.min

/**
 * @author Nicolas
 * @since 27/04/2021.
 */
class Food(private val location: Location, private var energy: Int = 200) : Attribute {
    val initialEnergy = energy

    init {
        location.addAttribute(this)
    }

    fun deplete(spoon: Int): Int {
        val result = min(energy, spoon)
        energy -= result
        if (energy <= 0) {
            location.attributes.remove(this)
        }
        return result
    }

    fun getEnergy(): Int {
        return energy
    }

    fun getRemainingPercent(): Int {
        return energy * 100 / initialEnergy
    }

    override val active: Boolean
        get() = energy > 0
}