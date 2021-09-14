package com.treil.kotai.creature

import com.treil.kotai.evolution.ScoreKeeper

/**
 * @author Nicolas
 * @since 20/04/2021.
 */
abstract class EnergyManager(val maxEnergy: Int, private val scoreKeeper: ScoreKeeper) {
    var energy: Int = maxEnergy

    fun useEnergy(e: Int) {
        energy -= e
        if (energy <= 0) {
            isDead()
        }
    }

    fun gainEnergy(i: Int) {
        energy += i
        if (energy > maxEnergy) {
            scoreKeeper.foodWaste(energy - maxEnergy);
            energy = maxEnergy // waste
        }
    }

    abstract fun isDead()
}