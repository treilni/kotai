package com.treil.kotai.brain

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TestMutator : Mutator {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(TestMutator::class.java.simpleName)
    }

    private var tick = 0
    override fun getMutationIndex(size: Int, reason: String): Int {
        val result = if (tick++ % 2 == 0) 0 else size - 1
        logger.info("getMutationIndex($size) $reason -> $result")
        return result
    }

    override fun getMutationCount(): Int {
        return 1
    }

    override fun getMutatedShort(n: Short, reason: String): Short {
        var unbounded = n.toInt()
        if (tick++ % 2 == 0) unbounded += 10 else unbounded -= 5
        logger.info("getMutationShort($n) $reason -> $unbounded")
        if (unbounded > Short.MAX_VALUE) return Short.MAX_VALUE
        if (unbounded < Short.MIN_VALUE) return Short.MIN_VALUE
        return unbounded.toShort()
    }

}
