package com.questmark.map

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable

/**
 * A manager for loading and rendering tiled maps from a Tiled .tmx format.
 *
 * A map is split into 3 graphical layers: bottom, middle, and top to add the feeling of
 * depth to the map for entities. There is also a collision layer that holds a list of
 * [com.badlogic.gdx.maps.MapObject] used for collision detection.
 *
 * The render order of the [TiledMapTileLayer] goes as followed: bottom, middle, top
 * with the [com.questmark.entity.systems.RenderSystem] rendered between middle and top.
 *
 * @author Ming Li
 */
class TileMapManager(batch: Batch, private val cam: OrthographicCamera) : Disposable {

    // loading
    private val mapLoader: TmxMapLoader = TmxMapLoader()

    // data
    private lateinit var tiledMap: TiledMap

    private lateinit var bottom: TiledMapTileLayer
    private lateinit var middle: TiledMapTileLayer
    private lateinit var top: TiledMapTileLayer
    private lateinit var collision: MapLayer

    companion object {

        // map layer keys
        private const val BOTTOM_LAYER = "bottom"
        private const val MIDDLE_LAYER = "middle"
        private const val TOP_LAYER = "top"
        private const val COLLISION_LAYER = "collision"

        // file path
        private const val DIR = "maps/"

    }

    // map properties (tile dimensions in pixels, map dimensions in tiles)
    var tileSize: Int = 0
        private set

    var mapWidth: Int = 0
        private set

    var mapHeight: Int = 0
        private set

    // rendering
    private val renderer: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(null, 1f, batch)

    val collisionBoxes: Array<Rectangle> = Array()

    /**
     * The tile map manager will be given a map name, which is the name of the tiled map
     * (not the file path). The filepath will be in some format "maps/[mapName].tmx"
     *
     * @param mapName name of the map
     */
    @Throws(IllegalStateException::class)
    fun load(mapName: String) {
        tiledMap = mapLoader.load("$DIR$mapName.tmx")

        // retrieve data from set properties in the map files
        tileSize = tiledMap.properties.get("tileSize", Int::class.java)
        mapWidth = tiledMap.properties.get("mapWidth", Int::class.java)
        mapHeight = tiledMap.properties.get("mapHeight", Int::class.java)

        // map layers
        bottom = tiledMap.layers.get(BOTTOM_LAYER) as TiledMapTileLayer
        middle = tiledMap.layers.get(MIDDLE_LAYER) as TiledMapTileLayer
        top = tiledMap.layers.get(TOP_LAYER) as TiledMapTileLayer
        collision = tiledMap.layers.get(COLLISION_LAYER)

        collisionBoxes.clear()
        val objects = collision.objects
        for (rectangleMapObject in objects.getByType(RectangleMapObject::class.java)) {
            val collisionBox = rectangleMapObject.rectangle
            collisionBoxes.add(collisionBox)
        }

        renderer.map = tiledMap
    }

    fun update() {
        // update camera
        renderer.setView(cam)

        // allow animated tiles to update
        AnimatedTiledMapTile.updateAnimationBaseTime()
    }

    /** Begin render functions. These must be between SpriteBatch.begin() and SpriteBatch.end()  */

    fun renderBottom() {
        renderer.renderTileLayer(bottom)
    }

    fun renderMiddle() {
        renderer.renderTileLayer(middle)
    }

    fun renderTop() {
        renderer.renderTileLayer(top)
    }

    /** End render functions  */

    override fun dispose() {
        renderer.dispose()
        tiledMap.dispose()
    }

}
