package com.treil.kotai.creature

import com.treil.kotai.evolution.ScoreKeeper
import com.treil.kotai.world.Point2D
import com.treil.kotai.world.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.ln

object MvtConstants {
    private const val MIN_MOVE = -1
    private const val MAX_MOVE = 2

    /* f(v) = a * log(v + 1) + b
    *  f(0) = b = 0
    *  f(MAX_VALUE) = a * log(MAX_VALUE + 1) = MAX_MOVE
    * */
    val APOSITIVE = MAX_MOVE.toDouble() / ln(Short.MAX_VALUE.toDouble() + 1)
    val ANEGATIVE = -MIN_MOVE.toDouble() / ln(-(Short.MIN_VALUE.toDouble()) + 1)
}

class MovementActuator(scoreKeeper: ScoreKeeper) : Actuator(scoreKeeper) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(MovementActuator::class.java.simpleName)
        val valueTable: Array<Int> = Array(Short.MAX_VALUE - Short.MIN_VALUE + 1) { i ->
            val v = i + Short.MIN_VALUE
            if (v >= 0)
                return@Array (MvtConstants.APOSITIVE * (ln(v.toDouble() + 1.0))).toInt()
            else
                return@Array (MvtConstants.ANEGATIVE * (ln(-v.toDouble() + 1.0))).toInt()
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
            if (v >= 0) {
                if (logger.isTraceEnabled) {
                    logger.trace("Moving to ${creature.facing} $speed times")
                }
            } else {
                direction = direction.opposite()
                if (logger.isTraceEnabled) {
                    logger.trace("Moving to ${creature.facing} ${-speed} times")
                }
            }

            if (speed != 0) {
                repeat(speed) {
                    position = position?.let { direction.move(it) }
                }
                if (position != null) {
                    val pos = position!!
                    try {
                        world.placeThingAt(creature, pos.x, pos.y)
                        if (logger.isDebugEnabled)
                            logger.debug("placed $creature at ${position?.x},${position?.y}")
                        scoreKeeper.successfulMove(speed, pos.x, pos.y)
                    } catch (e: Exception) {
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
