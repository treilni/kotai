package com.treil.kotai.brain

interface Mutator {
    fun getMutationIndex(size: Int, reason: String): Int
    fun getMutationCount(): Int
    fun getMutatedShort(n: Short, reason: String): Short
}
