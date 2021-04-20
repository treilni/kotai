package com.treil.kotai.brain

/**
 * @author Nicolas
 * @since 15/04/2021.
 */
interface HasValue {
    val value: Short
}

class StaticValue(override val value: Short) : HasValue
