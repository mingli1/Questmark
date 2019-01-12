package com.questmark.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.questmark.entity.ECS
import com.questmark.entity.Mapper
import com.questmark.entity.entities.Player
import com.questmark.entity.systems.KeyInputSystem
import com.questmark.game.Questmark
import com.questmark.input.KeyInput
import com.questmark.map.TileMapManager

/**
 * Represents the screen where all game play and map events are handled.
 *
 * @author Ming Li
 */
class GameScreen(game: Questmark) : AbstractScreen(game) {

    // input
    private val input: KeyInput

    // entity
    private val ecs: ECS = ECS(game.batch!!, game.res!!)
    private val player: Player

    // map
    private val tileMapManager: TileMapManager

    init {
        player = ecs.player!!
        tileMapManager = TileMapManager(game.batch!!, cam)

        // player input system
        val inputSystem = KeyInputSystem()
        input = KeyInput(inputSystem)
        ecs.addSystem(inputSystem)

        cam.zoom += 2f
    }

    override fun show() {
        Gdx.input.inputProcessor = input
        // load map on show
        tileMapManager.load("test")
        ecs.updateCollisionSystems(tileMapManager.mapWidth,
                tileMapManager.mapHeight, tileMapManager.tileSize, tileMapManager.collisionBoxes)
    }

    override fun update(dt: Float) {
        updateCamera()
        tileMapManager.update()
    }

    /**
     * Updates the [com.badlogic.gdx.graphics.OrthographicCamera] so that it follows the player's
     * current position except when the player moves to the edges of the map.
     */
    private fun updateCamera() {
        val pc = Mapper.POS_MAPPER!!.get(player)
        val mw = tileMapManager.mapWidth
        val mh = tileMapManager.mapHeight
        val ts = tileMapManager.tileSize

        if (pc.p.x <= mw * ts - (ts / 2 - 1) * ts && pc.p.x >= 6 * ts) {
            cam.position.x = pc.p.x + 8
        }
        if (pc.p.y <= mh * ts - ts / 4 * ts && pc.p.y >= 4 * ts - ts / 2) {
            cam.position.y = pc.p.y + 4
        }

        cam.update()

        if (pc.p.x < 6 * ts) cam.position.x = (6 * ts + ts / 2).toFloat()
        if (pc.p.y < 4 * ts - ts / 2) cam.position.y = (4 * ts - (ts / 2 - 1) / 2).toFloat()
        if (pc.p.x > mw * ts - 7 * ts) cam.position.x = (mw * ts - 7 * ts + 8).toFloat()
        if (pc.p.y > mh * ts - 4 * ts) cam.position.y = (mh * ts - 4 * ts + 4).toFloat()
    }

    override fun render(dt: Float) {
        update(dt)

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        game.batch!!.projectionMatrix = cam.combined
        game.batch!!.begin()

        tileMapManager.renderBottom()
        tileMapManager.renderMiddle()
        ecs.update(dt)
        tileMapManager.renderTop()

        game.batch!!.end()
    }

    override fun dispose() {
        super.dispose()
        ecs.dispose()
        tileMapManager.dispose()
    }

}
