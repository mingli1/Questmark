package com.questmark.entity.systems.collision

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array

/**
 * Represents a collision system that uses a map bounded [com.questmark.entity.QuadTree].
 *
 * @author Ming Li
 */
interface CollisionSystem {

    /**
     * Updates the collision system with map data.
     *
     * @param mapWidth the width of the map in tiles
     * @param mapHeight the height of the map in tiles
     * @param tileSize the size of each tile
     * @param boundingBoxes an array of tile bounding boxes
     */
    fun setMapData(mapWidth: Int, mapHeight: Int, tileSize: Int, boundingBoxes: Array<Rectangle>)

}
