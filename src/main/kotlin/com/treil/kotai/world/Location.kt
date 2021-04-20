package com.treil.kotai.world

open class Point2D(val x: Int, val y: Int)

class Location(x: Int, y: Int) : Point2D(x, y) {
    private var occupant: Thing? = null

    private val attributes: MutableList<Attribute> = ArrayList()

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

    fun getOccupant(): Thing? {
        return occupant
    }
}
