package com.treil.kotai.world

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Nicolas
 * @since 20/04/2021.
 *
 * Direction in degrees between 0 and 360
 */
class Direction(private var degrees: Short = 0) {
    constructor(other: Direction) : this(other.degrees)
    constructor(degrees: Int) : this(degrees.toShort())

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

    fun opposite(): Direction {
        val result = Direction(degrees)
        result.add(180)
        return result
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

    override fun toString(): String {
        return "Direction(degrees=$degrees)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Direction
        return degrees == other.degrees
    }

    override fun hashCode(): Int {
        return degrees.toInt()
    }

    fun getRawDegrees(): Short {
        return degrees
    }

    fun getStepDegrees(): Short {
        return (degrees - (degrees % 45)).toShort()
    }

}
