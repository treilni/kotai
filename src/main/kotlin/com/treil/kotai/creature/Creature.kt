package com.treil.kotai.creature

import com.treil.kotai.brain.Brain
import com.treil.kotai.brain.InputLayer
import com.treil.kotai.evolution.ScoreKeeper
import com.treil.kotai.world.Direction
import com.treil.kotai.world.Thing
import com.treil.kotai.world.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class Creature(val name: String, initialEnergy: Int, val scoreKeeper: ScoreKeeper) : Thing() {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(Creature::class.java.simpleName)
    }

    private val sensors: MutableList<Sensor> = ArrayList()
    private val actuators: MutableList<Actuator> = ArrayList()
    private var brain: Brain = Brain(InputLayer(), 0, 0) // temporary empty brain
    var facing = Direction()
    var dead = false

    private val energyManager = object : EnergyManager(initialEnergy, scoreKeeper) {
        override fun isDead() {
            if (logger.isDebugEnabled)
                logger.debug("Creature $name died")
            dead = true
        }
    }

    fun liveOneTick(world: World) {
        if (!dead) {
            sensors.forEach { sensor -> sensor.computeValue(world, this) }
            brain.compute()
            actuators.forEach { actuator -> actuator.act(world, this) }
            energyManager.useEnergy(1)
        }
    }

    fun addComponent(sensor: Sensor) {
        sensors.add(sensor)
    }

    fun addComponent(actuator: Actuator) {
        actuators.add(actuator)
    }

    fun wireBrain(vararg innerLayerSizes: Int) {
        // Assemble Input layer
        val inputLayer = InputLayer()
        sensors.forEach { sensor -> sensor.inputs.forEach { input -> inputLayer.add(input) } }

        val layerSizes: IntArray = innerLayerSizes.copyOf(innerLayerSizes.size + 1)
        // Add last layer for actuators
        val outputSize = actuators.fold(0) { acc, actuator -> acc + actuator.inputSize }
        layerSizes[layerSizes.lastIndex] = outputSize

        // create brain
        brain = Brain(inputLayer, *layerSizes)

        // wire actuators
        val outputSizes = actuators.map { actuator: Actuator -> actuator.inputSize }
        val actuatorsInputs = brain.getSplittedOutput(outputSizes)
        for ((index, value) in actuators.withIndex()) {
            value.inputLayer = actuatorsInputs[index]
        }
    }

    fun depleteEnergy(i: Int) {
        energyManager.useEnergy(i)
    }

    fun gainEnergy(i: Int) {
        energyManager.gainEnergy(i)
    }

    fun getEnergy(): Int {
        return energyManager.energy
    }

    fun getMaxEnergy(): Int {
        return energyManager.maxEnergy
    }

    fun getBrain(): Brain {
        return brain
    }

}
