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
import com.treil.kotai.render.EntityType
import com.treil.kotai.render.UserInterface
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
    const val TICK_MS: Long = 50
    val ANT_COLOR: Color = Color.RED
    val DYING_ANT_COLOR: Color = Color.BLACK
}

class WorldRenderer : GameApplication() {
    // living entities
    private val movingEntities: MutableMap<Creature, Entity> = HashMap()

    // depletable entities like food
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

    fun createAnt(dna: String, name: String = "Ant"): Ant {
        val ant = Ant(MovementScoreKeeper(), name, 200)
        ant.getBrain().setDNA(dna)
        ant.onDeath = {
            logger.info("Final score for ${it.name} : ${it.scoreKeeper.score}")
        }
        return ant
    }

    override fun initGame() {
        val world = Evolution.createWorld()
        scale = RenderingConstants.DISPLAY_WIDTH.toDouble() / world.width
        val random = Random(100)
        val dna = "10773/-2387C26510/-27084C12353/12738C-29112/-25836C871/-30759N11216/-12049C-13652/28937C1603/-10707C-24933/29441C-29611/26412N-2966/-4351C454/-3115C-6583/-20556C-26684/-7147C-303/5659N19419/4509C-15062/-15134C3396/24385C4511/5607C-23756/20970N30509/19537C19172/-2399C-21146/-22738C-13482/-30410C425/28925L-13056/-7358C-1410/-4044C22947/-28962C19806/4125C28709/5684N-19006/-16379C1916/8670C-22712/16960C-5768/-938C14444/29051N27140/20659C5871/-5628C-6778/9840C-26908/11048C25712/1638N22559/-3257C-10464/-19419C-3603/-5003C-18026/5870C24626/-2415N-32729/10260C29763/-20734C1457/10789C-15292/2181C-32691/-11082L2438/-20719C-24024/-27943C7990/-30495C24496/-20790C-2101/-23343N-5195/20396C3049/-14059C-30405/12034C26600/-9373C4443/-20971N28655/10091C-2603/-12916C10066/27809C-2308/28561C-21920/-32312"
        world.placeThingAtRandom(createAnt(dna, "Ant1"), random)
        world.placeThingAtRandom(createAnt(dna, "Ant2"), random)
        world.placeThingAtRandom(createAnt(dna, "Ant3"), random)
        world.placeThingAtRandom(createAnt(dna, "Ant4"), random)
        world.placeThingAtRandom(createAnt(dna, "Ant5"), random)

        // render content
        for (x in 0 until world.width) {
            for (y in 0 until world.height) {
                val location = world.getLocation(x, y)!!
                val occupant = location.getOccupant()
                if (occupant is Obstacle) {
                    FXGL.entityBuilder()
                        .type(EntityType.OBSTACLE)
                        .at(renderX(x), renderY(y))
                        .view(renderedObstacle())
                        .buildAndAttach()
                }
                location.attributes.forEach { attribute ->
                    val node = renderAttribute(attribute)
                    if (node != null) {
                        depletableEntities[attribute] = FXGL.entityBuilder()
                            .type(EntityType.DEPLETABLE)
                            .at(renderX(x), renderY(y))
                            .view(node)
                            .buildAndAttach()
                    }
                }
                if (occupant is Ant) {
                    movingEntities[occupant] = FXGL.entityBuilder()
                        .type(EntityType.LIVING)
                        .rotationOrigin(scale / 2 + 0.5, scale / 2 + 0.5)
                        .rotate(occupant.facing.getStepDegrees().toDouble())
                        .at(renderX(x), renderY(y))
                        .view(renderedAnt())
                        .buildAndAttach()
                }
            }
        }
        // render UI
        val userInterface = UserInterface(world, scale)
        FXGL.entityBuilder()
            .type(EntityType.UI)
            .at(0.0, renderY(-1))
            .view(userInterface.entity)
            .buildAndAttach()

        thread(start = true) {
            var tick = 0L;
            while (true) {
                var alive = false
                movingEntities.keys.forEach {
                    if (!it.dead) {
                        it.liveOneTick(world)
                        alive = true
                    }
                }
                if (++tick % 100 == 0L) {
                    logger.info("Lived {} ticks", tick)
                }
                if (!alive) {
                    logger.info("All creatures are dead, exiting.")
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