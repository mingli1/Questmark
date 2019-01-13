package com.questmark.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.questmark.screen.GameScreen
import com.questmark.util.Resources

/**
 * The main game class that manages game screens and assets.
 *
 * @author Ming Li
 */
class Questmark : Game() {

    lateinit var batch: Batch
    lateinit var res: Resources

    // screens
    private lateinit var gameScreen: GameScreen

    override fun create() {
        batch = SpriteBatch()
        res = Resources()

        gameScreen = GameScreen(this)
        this.setScreen(gameScreen)
    }

    override fun render() {
        Gdx.graphics.setTitle(Config.TITLE + " | " + Gdx.graphics.framesPerSecond + " fps")
        super.render()
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
        res.dispose()
    }

}
