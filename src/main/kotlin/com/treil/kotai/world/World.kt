package com.treil.kotai.world

import com.treil.kotai.creature.Creature
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Nicolas
 * @since 19/04/2021.
 */
class World(val width: Int, val height: Int) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(World::class.java.simpleName)
    }

    private val locations: Array<Array<Location>> = Array(height) { y -> Array(width) { x -> Location(x, y) } }
    private var creatures: MutableList<Creature> = ArrayList()

    init {
        // Frame world with obstacles
        for ((index, value) in locations.withIndex()) {
            val arrayOfLocations = value
            if (index == 0 || index == locations.lastIndex) {
                for (location in arrayOfLocations) {
                    location.setOccupant(Obstacle())
                }
            } else {
                arrayOfLocations[0].setOccupant(Obstacle())
                arrayOfLocations[arrayOfLocations.lastIndex].setOccupant(Obstacle())
            }
        }
    }

    fun placeThingAt(thing: Thing, x: Int, y: Int) {
        if (x < 0 || x >= width) {
            throw IllegalArgumentException("Illegal X([0-$width[) : $x")
        }
        if (y < 0 || y >= width) {
            throw IllegalArgumentException("Illegal Y([0-$height[) : $y")
        }
        val formerLocation = thing.location
        formerLocation?.removeOccupant()
        val location = getLocation(x, y)
        location?.setOccupant(thing)
    }

    fun getLocation(x: Int, y: Int): Location? {
        return locations.getOrNull(y)?.getOrNull(x)
    }
}