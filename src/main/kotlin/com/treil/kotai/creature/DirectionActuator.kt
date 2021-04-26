package com.treil.kotai.creature

import com.treil.kotai.evolution.ScoreKeeper
import com.treil.kotai.world.Direction
import com.treil.kotai.world.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DirectionActuator(scoreKeeper: ScoreKeeper) : Actuator(scoreKeeper) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(DirectionActuator::class.java.simpleName)
    }

    override fun act(world: World, creature: Creature) {
        val hasValue = inputLayer?.elements?.get(0)
        if (hasValue != null) {
            val oldFacing = creature.facing.getDegrees()
            val v = hasValue.value.toInt()
            val deltaDegrees = 45 * v / Short.MAX_VALUE
            val newDirection = Direction(creature.facing)
            newDirection.add(deltaDegrees.toShort())
            creature.facing = newDirection
            if (logger.isTraceEnabled) {
                logger.trace("Changed facing $oldFacing [$deltaDegrees] -> ${creature.facing.getDegrees()}")
            }
        }
    }
}
