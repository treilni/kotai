package com.treil.kotai.world

import java.lang.Integer.min
import kotlin.math.abs

/**
 * A position on a discrete map
 */
open class Point2D(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean {
        if (other is Point2D) {
            return x == other.x && y == other.y
        }
        return false
    }

    override fun toString(): String = "Point2D(x=$x, y=$y)"

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    fun cellDistance(x: Int, y: Int): Int = min(abs(this.x - x), abs(this.y - y))
}

/**
 * A location on the grid. A location can have a single Thing occupant, and several Attributes
 * @see Thing
 * @see Attribute
 */
class Location(x: Int, y: Int) : Point2D(x, y) {
    private var occupant: Thing? = null

    val attributes: MutableList<Attribute> = ArrayList()

    fun setOccupant(thing: Thing) {
        if (occupant != null) {
            throw IllegalAccessException("Occupied location at $x,$y")
        }
        occupant = thing
        thing.location = this
    }

    fun removeOccupant(): Thing? {
        val result = occupant
        occupant = null
        return result
    }

    fun getOccupant(): Thing? = occupant

    override fun toString(): String {
        val s = super.toString()
        return "Location(point=$s)"
    }

    fun addAttribute(attribute: Attribute) {
        attributes.add(attribute)
    }
}
