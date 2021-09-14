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
    const val TICK_MS: Long = 200
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
        val random = Random(100)
        val dna =
            "18124/5051C12714/19795C-5933/21686C-26434/8042C-3402/30223N25764/-16629C-1706/-13559C-3231/-30364C18409/-28861C-25535/-22285N-21770/-1188C13887/-17625C3729/-29682C26718/20375C11285/9084N-12258/-4381C5710/-21588C29061/276C-28223/29097C26531/-16948N-466/-18164C-32474/-25692C2365/29942C-926/32767C-24817/13807L7279/29192C-23465/10934C-9822/20147C8000/-32274C-32492/27035N-12215/-25688C-1781/-16076C26708/29495C13579/30192C-23217/-7007N-31066/30224C30502/-25329C-9418/24216C3186/12990C17191/-16469N17825/22039C-21215/24432C22658/-30821C20958/23288C-3629/3530N1653/-31141C-11388/-16352C12792/12249C-12278/24013C-21523/23055L3664/5188C26028/-21417C7467/-5446C-1251/27808C-20748/28272N-27821/-32412C13747/-4128C18969/6451C29320/-18885C3694/-32716N4777/423C360/-5783C-26749/-16138C-15635/-28449C10900/-20783"
        world.placeThingAtRandom(createAnt(dna), random)
        world.placeThingAtRandom(createAnt(dna), random)
        world.placeThingAtRandom(createAnt(dna), random)
        world.placeThingAtRandom(createAnt(dna), random)
        world.placeThingAtRandom(createAnt(dna), random)

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
                    } else if (it is Ant) {
                        logger.info("Final score : ${it.scoreKeeper.score}")
                    }
                }
                if (!alive) {
                    break
                }
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