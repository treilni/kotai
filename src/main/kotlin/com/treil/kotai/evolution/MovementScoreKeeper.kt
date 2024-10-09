package com.treil.kotai.evolution

/**
 * @author Nicolas
 * @since 23/04/2021.
 */
class MovementScoreKeeper : ScoreKeeper(0) {
    companion object {
        const val MOVE_SCORE = 5
        const val REVERSE_MOVE_SCORE = 2
        const val DISCOVERY_SCORE = 5
        const val BLOCKED_PENALTY = -1
        const val FOOD_WASTE_PENALTY = -5
    }

    val visited: MutableSet<String> = HashSet()

    override fun successfulMove(speed: Int, x: Int, y: Int) {
        if (speed > 0)
            score += MOVE_SCORE
        else if (speed < 0)
            score += REVERSE_MOVE_SCORE
        val locationKey = x.toString() + "/" + y.toString()
        if (!visited.contains(locationKey)) {
            visited.add(locationKey)
            score += DISCOVERY_SCORE
        }
        traceScore("successfulMove")
    }

    override fun changedFacing(from: Int, to: Int) {
        // slight direction change are encouraged ?
        score += 0
    }

    override fun unsuccessfulMove(speed: Int) {
        score += BLOCKED_PENALTY
        traceScore("unsuccessfulMove")
    }

    override fun foodWaste(wastedEnergy: Int) {
        score += FOOD_WASTE_PENALTY
        traceScore("foodWaste")
    }

    private fun traceScore(reason: String) {
        if (logger.isTraceEnabled)
            logger.trace("Score changed $reason => $score")
    }

}