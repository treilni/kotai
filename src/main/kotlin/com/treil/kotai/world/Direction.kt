package com.treil.kotai.world

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Nicolas
 * @since 20/04/2021.
 *
 * Direction in degrees
 */
class Direction(private var degrees: Short = 0) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(Direction::class.java.simpleName)
    }

    data class Delta(val x: Int, val y: Int)

    fun getDelta(): Delta {
        // For now use 45° ticks
        when (degrees / 45) {
            0 -> return Delta(0, 1)
            1 -> return Delta(1, 1)
            2 -> return Delta(1, 0)
            3 -> return Delta(1, -1)
            4 -> return Delta(0, -1)
            5 -> return Delta(-1, -1)
            6 -> return Delta(-1, 0)
            else -> return Delta(-1, 1)
        }
    }

    fun add(deltaDegrees: Short) {
        var result = (degrees + deltaDegrees) % 360
        if (result < 0) result += 360
        degrees = result.toShort()
    }

    fun move(point2D: Point2D): Point2D {
        val (dx, dy) = getDelta()
        return Point2D(point2D.x + dx, point2D.y + dy)
    }
}
