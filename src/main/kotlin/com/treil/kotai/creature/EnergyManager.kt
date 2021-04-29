package com.treil.kotai.creature

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
abstract class EnergyManager(private var energy: Int) {
    fun useEnergy(e: Int) {
        energy -= e
        if (energy <= 0) {
            isDead()
        }
    }

    fun gainEnergy(i: Int) {
        energy += i
    }

    abstract fun isDead()
    fun getEnergy(): Int {
        return energy
    }

}