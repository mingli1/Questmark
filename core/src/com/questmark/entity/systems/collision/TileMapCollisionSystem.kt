package com.questmark.entity.systems.collision

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.questmark.entity.Mapper
import com.questmark.entity.QuadTree
import com.questmark.entity.components.*

/**
 * A LibGDX Ashley [EntitySystem] that handles the collision between entities
 * and the tile map via their [BoundingBoxComponent] using axis aligned calculations.
 *
 * @author Ming Li
 */
class TileMapCollisionSystem :
        IteratingSystem(
                Family.all(BoundingBoxComponent::class.java, PositionComponent::class.java,
                        VelocityComponent::class.java, PreviousPositionComponent::class.java).get()
        ), CollisionSystem {

    private var quadTree: QuadTree? = null
    private val collisions: Array<Rectangle> = Array()

    // tile map bounding boxes
    private var mapWidth: Int = 0
    private var mapHeight: Int = 0
    private var tileSize: Int = 0

    override fun processEntity(entity: Entity, dt: Float) {
        val bb = Mapper.BOUNDING_BOX_MAPPER!!.get(entity)
        val position = Mapper.POS_MAPPER!!.get(entity)
        val velocity = Mapper.VEL_MAPPER!!.get(entity)
        val size = Mapper.SIZE_MAPPER!!.get(entity)
        val prevPosition = Mapper.PREV_POS_MAPPER!!.get(entity)
        bb.bounds.setPosition(position.p.x + (size.width - bb.bounds.width) / 2,
                position.p.y + (size.height - bb.bounds.height) / 2)

        // check for going outside of map
        if (position.p.x < 0 || position.p.x > (mapWidth - 1) * tileSize) {
            position.p.x = prevPosition.p.x
        }
        if (position.p.y < 0 || position.p.y > (mapHeight - 1) * tileSize) {
            position.p.y = prevPosition.p.y
        }

        collisions.clear()
        quadTree!!.retrieve(collisions, bb.bounds)

        for (bounds in collisions) {
            if (bb.bounds.overlaps(bounds)) {
                val bx = prevPosition.p.x + (size.width - bb.bounds.width) / 2
                val by = prevPosition.p.y + (size.height - bb.bounds.height) / 2
                if (bx + bb.bounds.width > bounds.x && bx <= bounds.x + bounds.width && velocity.v.y != 0f) {
                    position.p.y = prevPosition.p.y
                } else if (by + bb.bounds.height > bounds.y && by <= bounds.y + bounds.height && velocity.v.x != 0f) {
                    position.p.x = prevPosition.p.x
                }
            }
        }
    }

    override fun setMapData(mapWidth: Int, mapHeight: Int, tileSize: Int, boundingBoxes: Array<Rectangle>) {
        this.mapWidth = mapWidth
        this.mapHeight = mapHeight
        this.tileSize = tileSize

        quadTree = QuadTree(0, Rectangle(0f, 0f, (mapWidth * tileSize).toFloat(), (mapHeight * tileSize).toFloat()))
        // insert into quadtree only once in this instance because map bounding boxes don't change
        // currently. but if we decide map objects can move in the future (moving tiles) then it
        // will have to be inserted every update call
        for (bounds in boundingBoxes) {
            quadTree!!.insert(bounds)
        }
    }

}
