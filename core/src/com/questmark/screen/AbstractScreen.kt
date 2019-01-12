package com.questmark.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.questmark.game.Config
import com.questmark.game.Questmark

/**
 * A screen template for game states.
 *
 * @author Ming Li
 */
abstract class AbstractScreen(val game: Questmark) : Screen {

    // main stage of each screen
    var stage: Stage private set

    private var viewport: Viewport
    protected var cam: OrthographicCamera = OrthographicCamera(Config.V_WIDTH.toFloat(), Config.V_HEIGHT.toFloat())

    init {
        cam.setToOrtho(false)
        viewport = StretchViewport(Config.V_WIDTH.toFloat(), Config.V_HEIGHT.toFloat(), cam)
        stage = Stage(viewport, game.batch!!)
    }

    abstract fun update(dt: Float)

    override fun render(dt: Float) {
        stage.act(dt)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {}

    override fun show() {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        stage.dispose()
    }

}
