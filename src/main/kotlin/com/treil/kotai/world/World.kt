package com.treil.kotai.world

import com.treil.kotai.creature.Ant
import com.treil.kotai.creature.Creature
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.random.Random

/**
 * An grid of Locations. A world is framed with obstacles to prevent creatures from moving outside
 * @author Nicolas
 * @since 19/04/2021.
 * @see Location
 */
class World(
    val width: Int,
    val height: Int,
    obstaclePercent: Int = 0,
    foodPermille: Int = 0,
    seed: Long = System.currentTimeMillis()
) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(World::class.java.simpleName)
    }

    private val locations: Array<Array<Location>> = Array(height) { y -> Array(width) { x -> Location(x, y) } }

    init {
        // Frame world with obstacles
        for ((index, line) in locations.withIndex()) {
            if (index == 0 || index == locations.lastIndex) {
                for (location in line) {
                    location.setOccupant(Obstacle())
                }
            } else {
                line[0].setOccupant(Obstacle())
                line[line.lastIndex].setOccupant(Obstacle())
            }
        }

        // single obstacles
        val obstacles = (width - 2) * (height - 2) * obstaclePercent / 100
        val random = Random(seed)
        repeat(obstacles) {
            val x = random.nextInt(1, width - 2)
            val y = random.nextInt(1, height - 2)
            try {
                placeThingAt(Obstacle(), x, y)
            } catch (_: java.lang.Exception) {
            }
        }

        // food
        val foods = width * height * foodPermille / 1000
        repeat(foods) {
            try {
                val location = getLocation(random.nextInt(0, width), random.nextInt(0, height))
                if (location != null && location.getOccupant() == null) {
                    Food(location)
                }
            } catch (_: java.lang.Exception) {
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

    fun getLocationsAround(point2D: Point2D, radius: Int): Array<Location?> {
        val span = 2 * radius + 1
        val result = Array<Location?>(span * span) { null }
        var i = 0
        val y = point2D.y
        val x = point2D.x
        (y - radius..y + radius).forEach { yy ->
            (x - radius..x + radius).forEach { xx ->
                result[i++] = getLocation(xx, yy)
            }
        }
        return result
    }

    fun placeThingAtRandom(ant: Ant, random: Random) {
        while (true) {
            try {
                ant.facing = Direction(Random.nextInt(0, 360))
                placeThingAt(ant, random.nextInt(0, width), random.nextInt(0, height))
                return
            } catch (e: Exception) {
                if (logger.isDebugEnabled)
                    logger.debug("Could not place thing", e)
            }
        }
    }

    fun removeThing(creature: Creature) {
        creature.location?.removeOccupant()
    }
}