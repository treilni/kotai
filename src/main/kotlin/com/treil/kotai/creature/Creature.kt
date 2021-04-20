package com.treil.kotai.creature

import com.treil.kotai.brain.Brain
import com.treil.kotai.brain.InputLayer
import com.treil.kotai.world.Direction
import com.treil.kotai.world.Thing
import com.treil.kotai.world.World

open class Creature(initialEnergy: Int) : Thing() {
    private val sensors: MutableList<Sensor> = ArrayList()
    private val actuators: MutableList<Actuator> = ArrayList()
    private var brain: Brain = Brain(InputLayer(), 0, 0) // temporary empty brain
    var facing = Direction()

    private val energyManager = object : EnergyManager(initialEnergy) {
        override fun isDead() {
            TODO("Manage death")
        }
    }

    fun liveOneTick(world: World) {
        sensors.forEach { sensor -> sensor.computeValue(world, this) }
        actuators.forEach { actuator -> actuator.act(world, this) }
    }

    fun addComponent(sensor: Sensor) {
        sensors.add(sensor)
    }

    fun addComponent(actuator: Actuator) {
        actuators.add(actuator)
    }

    fun wireBrain(vararg layerSizes: Int) {
        // Assemble Input layer
        val inputLayer = InputLayer()
        sensors.forEach { sensor -> sensor.inputs.forEach { input -> inputLayer.add(input) } }
        brain = Brain(inputLayer, *layerSizes)
    }
}
