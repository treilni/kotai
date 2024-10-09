package com.treil.kotai.brain

/**
 * An entity with this interface can generate a DNA String description of its internal parameters
 * @author Nicolas
 * @since 15/04/2021.
 */
interface HasDNA {
    fun toDNA(): String
    fun mutate(mutator: Mutator)
}