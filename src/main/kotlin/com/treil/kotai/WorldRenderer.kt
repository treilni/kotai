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
        val dna = "-13202/-25859C1622/13411C-32542/-20716C-3209/-1967C3357/2726N-10302/-28289C16156/20108C-6098/2639C-4862/-16901C22091/-7915N792/1268C-5798/-1996C1250/-30543C-987/17489C10799/-10510N-16473/2232C24770/13254C81/-23146C3796/10202C-599/16925N2219/13619C-11372/-18732C-28610/27460C-29417/2506C-2428/29484L-9634/-19095C-31032/-4247C2483/20564C-5117/-19181C-21052/-30132N8408/25751C13873/29378C21460/22081C28986/3312C-11620/22255N-15173/-28874C-3534/-24296C-20136/-18915C28281/-24585C-12088/-2221N14531/27183C-23103/-11017C18335/20431C29892/12005C10671/11397N-4809/-19479C-8331/4134C-1059/14497C-29155/9551C25123/18461L18635/25232C926/3754C25020/-3688C-11593/-18882C-31182/16956N-24100/25180C-16998/-3889C-8638/29721C28112/19680C-12390/19876N23573/23106C26917/-29842C14617/-27951C-10172/7560C30453/31629"
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
                    // Render obstacles
                    FXGL.entityBuilder()
                        .type(EntityType.OBSTACLE)
                        .at(renderX(x), renderY(y))
                        .view(renderedObstacle())
                        .buildAndAttach()
                }
                location.attributes.forEach { attribute ->
                    // Render attributes (currently food)
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
                    // Render ants
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
            .at(0.0, renderY(userInterface.height))
            .view(userInterface.graphicalEntity)
            .buildAndAttach()

        thread(start = true) {
            var tick = 0L
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
        movingEntities.forEach { (creature, entity) ->
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

        depletableEntities.forEach { (attribute, entity) ->
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