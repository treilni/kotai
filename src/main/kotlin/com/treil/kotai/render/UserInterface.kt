package com.treil.kotai.render

import com.treil.kotai.world.World
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

/**
 * @author Nicolas
 * @since 14/09/2021.
 */
class UserInterface(world: World, scale: Double) {
    private val height: Int = 3
    val entity: Node = Rectangle(world.width * scale, height * scale, Color.BLACK)
}