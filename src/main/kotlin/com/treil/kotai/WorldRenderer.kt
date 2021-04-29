package com.treil.kotai

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getSettings
import com.almasb.fxgl.entity.Entity
import com.treil.kotai.RenderingConstants.ANT_COLOR
import com.treil.kotai.RenderingConstants.DYING_ANT_COLOR
import com.treil.kotai.RenderingConstants.TICK_MS
import com.treil.kotai.creature.Ant
import com.treil.kotai.creature.Creature
import com.treil.kotai.evolution.Evolution
import com.treil.kotai.evolution.MovementScoreKeeper
import com.treil.kotai.world.Attribute
import com.treil.kotai.world.Food
import com.treil.kotai.world.Obstacle
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Polygon
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.concurrent.thread
import kotlin.random.Random


/**
 * @author Nicolas
 * @since 14/04/2021.
 */

fun main(args: Array<String>) {
    GameApplication.launch(WorldRenderer::class.java, args)
}

object RenderingConstants {
    const val DISPLAY_WIDTH = 800
    const val DISPLAY_HEIGHT = 800
    const val TICK_MS: Long = 25
    val ANT_COLOR: Color = Color.RED
    val DYING_ANT_COLOR: Color = Color.BLACK
}

class WorldRenderer : GameApplication() {
    private val movingEntities: MutableMap<Creature, Entity> = HashMap()
    private val depletableEntities: MutableMap<Attribute, Entity> = HashMap()
    private var scale = 1.0

    companion object {
        val logger: Logger = LoggerFactory.getLogger(WorldRenderer::class.java.simpleName)
    }

    override fun initSettings(settings: GameSettings?) {
        settings?.width = RenderingConstants.DISPLAY_WIDTH
        settings?.height = RenderingConstants.DISPLAY_HEIGHT
        settings?.title = "KotAI"
    }

    fun renderX(x: Int): Double {
        return x * scale
    }

    fun renderY(y: Int): Double {
        return getSettings().height - (y + 1) * scale
    }

    fun renderX(x: Double): Double {
        return x * scale
    }

    fun renderY(y: Double, relativeHeight: Double): Double {
        return relativeHeight - y * scale
    }

    fun createAnt(dna: String): Ant {
        val ant = Ant(MovementScoreKeeper(), 200)
        ant.getBrain().setDNA(dna)
        return ant
    }

    override fun initGame() {
        //val world = World(20, 20, 7, 50)
        val world = Evolution.createWorld()
        scale = RenderingConstants.DISPLAY_WIDTH.toDouble() / world.width
        val random = Random(0)
        val dna =
            "25654/29150C-29521/440N-19235/10563C13702/-598N19583/18538C-30453/29202N14892/-26676C29838/-823L-24689/-9786C20760/28376C-2968/9751C21036/-2330N4521/-12283C-30984/18913C18162/-3277C24259/29424N25187/23092C25044/6203C24327/1027C-3378/31244"
        world.placeThingAtRandom(createAnt(dna), random)
//        world.placeThingAtRandom(createAnt(dna), random)
//        world.placeThingAtRandom(createAnt(dna), random)
//        world.placeThingAtRandom(createAnt(dna), random)
//        world.placeThingAtRandom(createAnt(dna), random)

        // render content
        for (x in 0 until world.width) {
            for (y in 0 until world.height) {
                val location = world.getLocation(x, y)!!
                val occupant = location.getOccupant()
                if (occupant is Obstacle) {
                    FXGL.entityBuilder()
                        .at(renderX(x), renderY(y))
                        .view(renderedObstacle())
                        .buildAndAttach()
                }
                location.attributes.forEach { attribute ->
                    val node = renderAttribute(attribute)
                    if (node != null) {
                        depletableEntities[attribute] = FXGL.entityBuilder()
                            .at(renderX(x), renderY(y))
                            .view(node)
                            .buildAndAttach()
                    }
                }
                if (occupant is Ant) {
                    movingEntities[occupant] = FXGL.entityBuilder()
                        .rotationOrigin(scale / 2 + 0.5, scale / 2 + 0.5)
                        .rotate(occupant.facing.getStepDegrees().toDouble())
                        .at(renderX(x), renderY(y))
                        .view(renderedAnt())
                        .buildAndAttach()
                }
            }
        }

        thread(start = true) {
            while (true) {
                var alive = false
                movingEntities.keys.forEach {
                    if (!it.dead) {
                        it.liveOneTick(world)
                        alive = true
                    }
                }
                if (!alive) break
                Thread.sleep(TICK_MS)
            }
        }
    }

    private fun renderAttribute(attribute: Attribute): Node? {
        if (attribute is Food) {
            val circle = Circle(scale / 2, Color.LAWNGREEN)
            circle.centerX = scale / 2
            circle.centerY = scale / 2
            return circle
        }
        return null
    }

    private fun renderedAnt(): Node {
        val result = Polygon(
            renderX(0), renderY(0.0, scale),
            renderX(0.5), renderY(1.0, scale),
            renderX(1), renderY(0.0, scale)
        )
        result.fill = ANT_COLOR
        return result
    }

    override fun onUpdate(tpf: Double) {
        super.onUpdate(tpf)
        movingEntities.forEach() { (creature, entity) ->
            val location = creature.location
            if (location != null) {
                val energy = creature.getEnergy()
                if (energy < 500) {
                    entity.viewComponent.children.filterIsInstance<Shape>().forEach {
                        it.fill = DYING_ANT_COLOR.interpolate(ANT_COLOR, energy / 500.0)
                    }
                } else {
                    entity.viewComponent.children.filterIsInstance<Shape>().forEach {
                        it.fill = ANT_COLOR
                    }
                }
                entity.setPosition(renderX(location.x), renderY(location.y))
                entity.rotation = creature.facing.getStepDegrees().toDouble()
            }
        }

        depletableEntities.forEach() { (attribute, entity) ->
            if (attribute is Food) {
                entity.setScaleUniform(attribute.getRemainingPercent() / 100.0)
            }
            if (!attribute.active) {
                entity.removeFromWorld()
            }
        }
    }

    private fun renderedObstacle() = Rectangle(scale, scale, Color.GRAY)
}