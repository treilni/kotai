package com.treil.kotai.brain

/**
 * An interface which encapsulate a value in short format
 * @author Nicolas
 * @since 15/04/2021.
 */
interface HasValue {
    val value: Short
}

/**
 * An instance of HasValue which always supplies the same static value
 */
class StaticValue(override val value: Short) : HasValue
