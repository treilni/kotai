package com.treil.kotai.creature

import com.treil.kotai.brain.HasValue
import com.treil.kotai.world.Point2D
import com.treil.kotai.world.World

object Constants {
    const val VISION_RANGE = 3
}

class EyeSensor : Sensor() {
    private data class EyeSensorValue(override var value: Short) : HasValue

    private var forwardValue = EyeSensorValue(0)

    init {
        inputs.add(forwardValue)
    }

    override fun computeValue(world: World, creature: Creature) {
        val location: Point2D? = creature.location
        if (location == null) {
            forwardValue.value = 0
        } else {
            val step = Short.MAX_VALUE / Constants.VISION_RANGE
            var possibleReturn: Int = Short.MAX_VALUE.toInt()
            var point = Point2D(location.x, location.y)
            for (i in 1..Constants.VISION_RANGE) {
                point = creature.facing.move(point)
                val nextLoc = world.getLocation(point.x, point.y)
                if (nextLoc == null || nextLoc.getOccupant() != null) {
                    forwardValue.value = possibleReturn.toShort()
                    return
                }
                possibleReturn -= step
            }
            forwardValue.value = 0
        }
    }
}
