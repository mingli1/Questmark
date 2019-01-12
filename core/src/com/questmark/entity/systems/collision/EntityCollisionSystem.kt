package com.questmark.entity.systems.collision

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.questmark.entity.Mapper
import com.questmark.entity.QuadTree
import com.questmark.entity.components.*
import com.questmark.entity.components.enemy.EnemyComponent

/**
 * A LibGDX Ashley [EntitySystem] that handles the collision between any two entities
 * via their [com.questmark.entity.components.BoundingBoxComponent] using axis aligned calculations.
 *
 * @author Ming Li
 */
class EntityCollisionSystem : EntitySystem(), CollisionSystem {

    private var quadTree: QuadTree? = null
    private var collisions: Array<Rectangle>? = null

    private var entities: ImmutableArray<Entity>? = null

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(BoundingBoxComponent::class.java, PositionComponent::class.java,
                VelocityComponent::class.java, PreviousPositionComponent::class.java).exclude(EnemyComponent::class.java).get())
        collisions = Array()
    }

    override fun update(dt: Float) {
        quadTree!!.clear()
        for (e in entities!!) {
            val b = Mapper.BOUNDING_BOX_MAPPER!!.get(e)
            quadTree!!.insert(b.bounds)
        }

        for (e in entities!!) {
            val bb = Mapper.BOUNDING_BOX_MAPPER!!.get(e)
            val p = Mapper.POS_MAPPER!!.get(e)
            val v = Mapper.VEL_MAPPER!!.get(e)
            val pp = Mapper.PREV_POS_MAPPER!!.get(e)

            collisions!!.clear()
            quadTree!!.retrieve(collisions, bb.bounds)

            for (bounds in collisions!!) {
                if (bb.bounds != bounds) {
                    if (bb.bounds.overlaps(bounds)) {
                        v.v.set(0f, 0f)
                        p.p.set(pp.p.x, pp.p.y)
                    }
                }
            }
        }
    }

    override fun setMapData(mapWidth: Int, mapHeight: Int, tileSize: Int, boundingBoxes: Array<Rectangle>) {
        quadTree = QuadTree(0, Rectangle(0f, 0f, (mapWidth * tileSize).toFloat(), (mapHeight * tileSize).toFloat()))
    }

}
