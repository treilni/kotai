package com.treil.kotai

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.Entity
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import org.slf4j.LoggerFactory


/**
 * @author Nicolas
 * @since 14/04/2021.
 */
@Deprecated("For basic test only")
fun main(args: Array<String>) {
    val application = Application()
    application.launchApp(args);
}

class Application : GameApplication() {
    private var player: Entity? = null;

    companion object {
        val logger = LoggerFactory.getLogger(Application::class.java.getSimpleName())
    }

    override fun initSettings(settings: GameSettings?) {
        settings?.width = 1024;
        settings?.height = 768;
        settings?.title = "KotAI";
    }

    override fun initGame() {
        player = FXGL.entityBuilder()
            .at(300.0, 300.0)
            .view(renderedPlayer())
            .buildAndAttach()
    }

    private fun renderedPlayer() = Rectangle(25.0, 25.0, Color.BLUE)

    fun launchApp(args: Array<String>) {
        logger.info("Launching")
        launch(args)
    }
}