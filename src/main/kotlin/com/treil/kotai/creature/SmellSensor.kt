package com.treil.kotai.creature

import com.treil.kotai.world.Direction
import com.treil.kotai.world.Food
import com.treil.kotai.world.Point2D
import com.treil.kotai.world.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.atan
import kotlin.math.max
import kotlin.math.round

class SmellSensor : Sensor() {
    companion object {
        const val SMELL_RANGE = 2
        const val STRENGTH_NORMALIZATION = 10
        const val BEARING_NORMALIZATION: Short = (Short.MAX_VALUE / 180).toShort()

        val logger: Logger = LoggerFactory.getLogger(SmellSensor::class.java.simpleName)
    }

    private val relativeBearing = NormalizedValue(0, BEARING_NORMALIZATION)
    private val strength = Value(0)

    init {
        inputs.add(relativeBearing)
        inputs.add(strength)
    }

    override fun computeValue(world: World, creature: Creature) {
        val position: Point2D? = creature.location
        if (position != null) {
            val locations = world.getLocationsAround(position, SMELL_RANGE)
            // barycenter
            val ymax = SMELL_RANGE
            val xmax = SMELL_RANGE
            var i = 0
            var xValue = 0
            var yValue = 0
            var totalFood = 0
            for (y in -ymax..ymax) {
                for (x in -xmax..xmax) {
                    locations[i++]?.let {
                        totalFood += it.attributes.filterIsInstance<Food>()
                            .map { food -> food.getEnergy() }
                            .filter { energy -> energy > 0 }
                            .fold(0) { acc, energy ->
                                xValue += x * energy
                                yValue += y * energy
                                val distance = (max(kotlin.math.abs(x), kotlin.math.abs(y)) + 1)
                                return@fold acc + energy / distance
                            }
                    }
                }
            }
            strength.value = (totalFood * STRENGTH_NORMALIZATION).toShort()
            if (xValue == 0) {
                relativeBearing.unnormalizedValue = 0
            } else {
                val angle = atan(yValue.toDouble() / xValue)
                val delta = let { if (xValue > 0) -90 else 90 }
                val absoluteDirection = Direction(round(angle * 180 / Math.PI).toInt() + delta)
                absoluteDirection.add((-creature.facing.getRawDegrees()).toShort())
                var rawDegrees = absoluteDirection.getRawDegrees()
                if (rawDegrees > 180) rawDegrees = (rawDegrees - 360).toShort()
                relativeBearing.unnormalizedValue = rawDegrees
            }
            if (logger.isTraceEnabled)
                logger.trace("bearing = ${relativeBearing.unnormalizedValue} -> ${relativeBearing.value}")
        }
    }

    fun getRelativeBearing() = relativeBearing.unnormalizedValue
    fun getStrength() = strength.value
}
