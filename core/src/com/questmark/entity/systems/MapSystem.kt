package com.questmark.entity.systems

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array

/**
 * Represents a map system that handles component interactions with the map.
 *
 * @author Ming Li
 */
interface MapSystem {

    /**
     * Updates the map system with map data.
     *
     * @param mapWidth the width of the map in tiles
     * @param mapHeight the height of the map in tiles
     * @param tileSize the size of each tile
     * @param boundingBoxes an array of tile bounding boxes
     */
    fun setMapData(mapWidth: Int, mapHeight: Int, tileSize: Int, boundingBoxes: Array<Rectangle>)

}

