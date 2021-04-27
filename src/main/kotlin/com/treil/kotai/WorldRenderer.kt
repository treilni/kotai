package com.treil.kotai

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getSettings
import com.almasb.fxgl.entity.Entity
import com.treil.kotai.creature.Ant
import com.treil.kotai.creature.Creature
import com.treil.kotai.evolution.Evolution
import com.treil.kotai.evolution.MovementScoreKeeper
import com.treil.kotai.world.Obstacle
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon
import javafx.scene.shape.Rectangle
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
    const val SCALE = 20.0
    const val DISPLAY_WIDTH = 800
    const val DISPLAY_HEIGHT = 800
}

class WorldRenderer : GameApplication() {
    val movingEntities: MutableMap<Creature, Entity> = HashMap()

    companion object {
        val logger = LoggerFactory.getLogger(WorldRenderer::class.java.getSimpleName())
    }

    override fun initSettings(settings: GameSettings?) {
        settings?.width = RenderingConstants.DISPLAY_WIDTH
        settings?.height = RenderingConstants.DISPLAY_HEIGHT
        settings?.title = "KotAI"
    }

    fun renderX(x: Int): Double {
        return x * RenderingConstants.SCALE
    }

    fun renderY(y: Int): Double {
        return getSettings().height - (y + 1) * RenderingConstants.SCALE
    }

    fun renderX(x: Double): Double {
        return x * RenderingConstants.SCALE
    }

    fun renderY(y: Double, relativeHeight: Double): Double {
        return relativeHeight - y * RenderingConstants.SCALE
    }

    override fun initGame() {
        val world = Evolution.createWorld()
        val ant = Ant(MovementScoreKeeper())
        world.placeThingAtRandom(ant, Random(0))

        // render content
        for (x in 0 until world.width) {
            for (y in 0 until world.height) {
                val occupant = world.getLocation(x, y)?.getOccupant()
                if (occupant is Obstacle) {
                    FXGL.entityBuilder()
                        .at(renderX(x), renderY(y))
                        .view(renderedObstacle())
                        .buildAndAttach()
                }
                if (occupant is Ant) {
                    movingEntities.put(
                        occupant,
                        FXGL.entityBuilder()
                            .rotationOrigin(RenderingConstants.SCALE / 2 + 0.5, RenderingConstants.SCALE / 2 + 0.5)
                            .rotate(occupant.facing.getStepDegrees().toDouble())
                            .at(renderX(x), renderY(y))
                            .view(renderedAnt())
                            .buildAndAttach()
                    )
                }
            }
        }

        val dna = "5916/-4472N-13435/24104L-15599/7925C-2956/-3135N20490/-9976C8018/-26192"
        ant.getBrain().setDNA(dna)
        thread(start = true) {
            while (!ant.dead) {
                Thread.sleep(1000)
                ant.liveOneTick(world)
            }
        }
    }

    private fun renderedAnt(): Node {
        val result = Polygon(
            renderX(0), renderY(0.0, RenderingConstants.SCALE),
            renderX(0.5), renderY(1.0, RenderingConstants.SCALE),
            renderX(1), renderY(0.0, RenderingConstants.SCALE)
        )
        result.fill = Color.RED
        return result
    }

    override fun onUpdate(tpf: Double) {
        super.onUpdate(tpf)
        movingEntities.forEach() { (creature, entity) ->
            val location = creature.location
            if (location != null) {
                entity.setPosition(renderX(location.x), renderY(location.y))
                entity.rotation = creature.facing.getStepDegrees().toDouble()
            }
        }
    }

    private fun renderedObstacle() = Rectangle(RenderingConstants.SCALE, RenderingConstants.SCALE, Color.GRAY)
}