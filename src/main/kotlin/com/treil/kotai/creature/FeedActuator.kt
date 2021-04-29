package com.treil.kotai.creature

import com.treil.kotai.evolution.ScoreKeeper
import com.treil.kotai.world.Food
import com.treil.kotai.world.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Nicolas
 * @since 28/04/2021.
 */
class FeedActuator(scoreKeeper: ScoreKeeper) : Actuator(scoreKeeper) {
    companion object {
        const val SPOON = 75
        const val FEED_ENERGY_COST = 1
        const val FEED_THRESHOLD = Short.MAX_VALUE / 2

        val logger: Logger = LoggerFactory.getLogger(FeedActuator::class.java.simpleName)
    }

    override fun act(world: World, creature: Creature) {
        val value = inputLayer?.elements?.get(0)?.value
        if (logger.isTraceEnabled) {
            logger.trace("Input value : $value")
        }
        value?.let {
            if (it > FEED_THRESHOLD) {
                val location = creature.location
                if (location != null) {
                    location.attributes
                        .filterIsInstance<Food>()
                        .firstOrNull()?.deplete(SPOON)?.let {
                            if (logger.isTraceEnabled)
                                logger.trace("Found food : $it energy")
                            creature.gainEnergy(it)
                        }
                    // feeding costs energy even if no food found
                    creature.depleteEnergy(FEED_ENERGY_COST)
                }
            }
        }

    }
}