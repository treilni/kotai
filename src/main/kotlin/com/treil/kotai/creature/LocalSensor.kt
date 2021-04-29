package com.treil.kotai.creature

import com.treil.kotai.world.Attribute
import com.treil.kotai.world.Food
import com.treil.kotai.world.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LocalSensor : Sensor() {
    private companion object {
        const val FOOD_NORMALIZATION = 20

        val logger: Logger = LoggerFactory.getLogger(LocalSensor::class.java.simpleName)
    }

    private val foodValue = Value(0)

    init {
        inputs.add(foodValue)
    }

    override fun computeValue(world: World, creature: Creature) {
        val attributes = creature.location?.attributes
        if (attributes is List<Attribute>) {
            foodValue.value = attributes.filterIsInstance<Food>()
                .fold(0) { acc, food -> acc + food.getEnergy() * FOOD_NORMALIZATION }
                .toShort()
        }
        if (logger.isTraceEnabled)
            logger.trace("Food value : ${foodValue.value}")
    }
}
