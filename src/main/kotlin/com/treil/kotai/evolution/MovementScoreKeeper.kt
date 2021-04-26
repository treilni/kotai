package com.treil.kotai.evolution

/**
 * @author Nicolas
 * @since 23/04/2021.
 */
class MovementScoreKeeper : ScoreKeeper(0) {
    companion object {
        const val DISTANCE_SCORE = 10
        const val DISCOVERY_SCORE = 5
        const val BLOCKED_PENALTY = -1
    }

    val visited: MutableSet<String> = HashSet()

    override fun successfulMove(distance: Int, x: Int, y: Int) {
        score += distance * DISTANCE_SCORE
        val locationKey = x.toString() + "/" + y.toString()
        if (!visited.contains(locationKey)) {
            visited.add(locationKey)
            score += DISCOVERY_SCORE
        }
    }

    override fun changedFacing(from: Int, to: Int) {
        // slight direction change are encouraged ?
    }

    override fun unsuccessfulMove(speed: Int) {
        score += BLOCKED_PENALTY
    }

}