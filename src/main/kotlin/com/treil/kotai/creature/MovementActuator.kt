package com.treil.kotai.creature

import com.treil.kotai.world.Point2D
import com.treil.kotai.world.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object MvtConstants {
    const val MIN_MOVE = -1
    const val MAX_MOVE = 2
}

class MovementActuator : Actuator() {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(MovementActuator::class.java.simpleName)
    }

    override fun act(world: World, creature: Creature) {
        var position: Point2D? = creature.location
        if (position == null) {
            logger.warn("Creature ${creature.name} has no current location")
            return
        }
        val hasValue = inputLayer?.elements?.get(0)
        if (hasValue != null) {
            val v = hasValue.value.toInt()
            var direction = creature.facing
            var speed: Int
            if (v >= 0) {
                speed = (v * (MvtConstants.MAX_MOVE + 1)) / (Short.MAX_VALUE + 1)
            } else {
                speed = (-v * (MvtConstants.MIN_MOVE - 1)) / (Short.MIN_VALUE - 1)
                direction = direction.opposite()
            }

            if (speed != 0) {
                if (logger.isDebugEnabled) {
                    logger.debug("Moving $position to $direction $speed times")
                }
                while (speed-- > 0) {
                    position = position?.let { direction.move(it) }
                }
                if (position != null) {
                    world.placeThingAt(creature, position.x, position.y)
                }
            } else {
                if (logger.isDebugEnabled) {
                    logger.debug("Zero speed, not moving")
                }
            }
        }
    }
}
