package com.treil.kotai.creature

import com.treil.kotai.evolution.ScoreKeeper
import com.treil.kotai.world.Point2D
import com.treil.kotai.world.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.round
import kotlin.math.sign

class MovementActuator(scoreKeeper: ScoreKeeper) : Actuator(scoreKeeper) {
    companion object {
        private const val MIN_MOVE = -1
        private const val MAX_MOVE = 1

        /* f(v) = a * log(v + 1) + b
        *  f(0) = b = MIN_MOVE
        *  f(MAX_VALUE) = a * MAX_VALUE + MIN_MOVE = MAX_MOVE
        *  a = (MAX_MOVE - MIN_MOVE) / MAX_VALUE
        * */
        val logger: Logger = LoggerFactory.getLogger(MovementActuator::class.java.simpleName)
        val valueTable: Array<Int> = Array(Short.MAX_VALUE - Short.MIN_VALUE + 1) { i ->
            round((MAX_MOVE - MIN_MOVE).toDouble() * i.toDouble() / (Short.MAX_VALUE - Short.MIN_VALUE)).toInt() + MIN_MOVE
        }
    }

    override fun act(world: World, creature: Creature) {
        var position: Point2D? = creature.location
        if (position == null) {
            logger.warn("Creature ${creature.name} has no current location")
            return
        }
        val hasValue = inputLayer?.elements?.get(0)
        if (hasValue != null) {
            if (logger.isTraceEnabled) {
                logger.trace("Got ${hasValue.value} from $hasValue")
            }
            val v = hasValue.value.toInt()
            var direction = creature.facing
            val speed = valueTable[v - Short.MIN_VALUE]
            var moveCount = speed
            if (speed >= 0) {
                if (logger.isTraceEnabled) {
                    logger.trace("Moving forward ${creature.name} to ${creature.facing} $moveCount times")
                }
            } else {
                moveCount = -speed
                direction = direction.opposite()
                if (logger.isTraceEnabled) {
                    logger.trace("Moving backward ${creature.name} to ${creature.facing} $moveCount times")
                }
            }

            if (moveCount > 0) {
                repeat(moveCount) {
                    position = position?.let { direction.move(it) }
                }
                if (position != null) {
                    val pos = position!!
                    try {
                        world.placeThingAt(creature, pos.x, pos.y)
                        if (logger.isDebugEnabled)
                            logger.debug("placed $creature at ${position?.x},${position?.y}")
                        scoreKeeper.successfulMove(speed * v.sign, pos.x, pos.y)
                    } catch (_: Exception) {
                        if (logger.isDebugEnabled)
                            logger.debug("Failed placing $creature at ${position?.x},${position?.y}")
                        scoreKeeper.unsuccessfulMove(speed)
                    }
                }
            } else {
                if (logger.isDebugEnabled) {
                    logger.debug("Zero speed, not moving")
                }
            }
        }
    }
}
