package com.treil.kotai.evolution

/**
 * @author Nicolas
 * @since 23/04/2021.
 */
abstract class ScoreKeeper(var score: Int = 0) {
    abstract fun successfulMove(speed: Int, x: Int, y: Int)
    abstract fun changedFacing(from: Int, to: Int)
    abstract fun unsuccessfulMove(speed: Int)

}