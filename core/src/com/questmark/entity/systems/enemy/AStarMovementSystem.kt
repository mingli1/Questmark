package com.questmark.entity.systems.enemy

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.questmark.entity.Mapper
import com.questmark.entity.components.*
import com.questmark.entity.components.enemy.AggressionComponent
import com.questmark.entity.components.enemy.EnemyComponent
import com.questmark.entity.components.enemy.SourcePositionComponent
import com.questmark.entity.components.player.PlayerComponent
import com.questmark.entity.systems.MapSystem
import com.questmark.pathfinding.AStar
import com.questmark.pathfinding.Node

import java.util.HashMap

/**
 * A LibGDX Ashley [EntitySystem] that handles the movement of enemies. In this system,
 * enemies follows the player using a path calculated by the A* search algorithm.
 *
 * @author Ming Li
 */
class AStarMovementSystem : IteratingSystem(Family.all(EnemyComponent::class.java, AggressionComponent::class.java, SourcePositionComponent::class.java).get()), MapSystem {

    private lateinit var alg: AStar
    private lateinit var collidableEntities: ImmutableArray<Entity>
    private lateinit var mapCollisions: Array<Rectangle>
    private val allCollisions: Array<Rectangle> = Array()
    private var tileSize: Int = 0

    private lateinit var player: Entity
    private val paths: MutableMap<Entity, Array<Node>> = HashMap()
    private val timers: MutableMap<Entity, Float> = HashMap()
    private val freqs: MutableMap<Entity, Float> = HashMap()
    private val toSource: MutableMap<Entity, Boolean> = HashMap()
    private val returnPaths: MutableMap<Entity, Array<Node>> = HashMap()

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        for (e in this.entities) {
            timers[e] = 0f
            toSource[e] = false
        }
        player = engine.getEntitiesFor(Family.all(PlayerComponent::class.java).get()).get(0)
        collidableEntities = engine.getEntitiesFor(Family.all(BoundingBoxComponent::class.java)
                .exclude(EnemyComponent::class.java, PlayerComponent::class.java).get())
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val agg = Mapper.AGGRESSION_MAPPER.get(entity)
        val pos = Mapper.POS_MAPPER.get(entity)
        val playerPos = Mapper.POS_MAPPER.get(player)
        val vel = Mapper.VEL_MAPPER.get(entity)
        val mag = Mapper.SPEED_MAPPER.get(entity)

        // player within aggression range
        if (pos.p.dst(playerPos.p) <= agg.range || agg.range == -1f) {
            agg.atSource = false
            toSource[entity] = false
            timers[entity] = timers[entity]!! + deltaTime

            if (timers[entity]!! > freqs[entity]!!) {
                val source = Vector2((Math.round(pos.p.x / tileSize) * tileSize).toFloat(),
                        (Math.round(pos.p.y / tileSize) * tileSize).toFloat())
                val target = Vector2((Math.round(playerPos.p.x / tileSize) * tileSize).toFloat(),
                        (Math.round(playerPos.p.y / tileSize) * tileSize).toFloat())
                if (source == target)
                    vel.v.set(0f, 0f)
                else
                    paths[entity] = getPath(entity, source, target)!!
                timers[entity] = timers[entity]!! - freqs[entity]!!
            }

            val path = paths[entity]
            if (path != null) {
                if (path.size > 0) {
                    val target = path.get(path.size - 1).position
                    move(pos.p, target, vel.v, mag.speed, deltaTime)
                }
            }
        } else {
            val source = Mapper.SOURCE_POS_MAPPER.get(entity)
            val sx = source.s.x.toInt() / tileSize * tileSize
            val sy = source.s.y.toInt() / tileSize * tileSize

            if (!toSource[entity]!!) {
                val s = Vector2(sx.toFloat(), sy.toFloat())
                returnPaths[entity] = getPath(entity, Vector2((Math.round(pos.p.x / tileSize) * tileSize).toFloat(),
                        (Math.round(pos.p.y / tileSize) * tileSize).toFloat()), s)!!
                toSource[entity] = true
            }

            val returnPath = returnPaths[entity]
            if (returnPath != null) {
                if (returnPath.size > 0) {
                    val target = returnPath.get(returnPath.size - 1).position
                    if (sx.toFloat() == target.x && sy.toFloat() == target.y) {
                        move(pos.p, source.s, vel.v, mag.speed, deltaTime)
                        if (pos.p == source.s) returnPath.removeIndex(returnPath.size - 1)
                    } else {
                        move(pos.p, target, vel.v, mag.speed, deltaTime)
                        if (pos.p == target) returnPath.removeIndex(returnPath.size - 1)
                    }
                }
            }
            if (pos.p == source.s) agg.atSource = true
        }
    }

    override fun setMapData(mapWidth: Int, mapHeight: Int, tileSize: Int, boundingBoxes: Array<Rectangle>) {
        this.alg = AStar(mapWidth, mapHeight, tileSize)
        this.mapCollisions = boundingBoxes
        this.tileSize = tileSize
        for (e in this.entities) {
            val mag = Mapper.SPEED_MAPPER.get(e)
            freqs[e] = tileSize / 2 / mag.speed
        }
    }

    /**
     * Runs the A* algorithm with a given source and target on a given entity.
     * Helper method that does the initial collision data setup.
     *
     * @param e
     * @param source
     * @param target
     * @return
     */
    private fun getPath(e: Entity, source: Vector2, target: Vector2): Array<Node>? {
        allCollisions.clear()
        allCollisions.addAll(mapCollisions)
        for (entity in collidableEntities) {
            if (e != entity) {
                val bb = Mapper.BOUNDING_BOX_MAPPER.get(e)
                allCollisions.add(bb.bounds)
            }
        }
        alg.setCollisionData(allCollisions)
        return alg.findPath(source, target)
    }

    /**
     * Moves an entity from a source to a target by updating its velocity.
     *
     * @param source
     * @param target
     * @param vel
     * @param speed
     * @param dt
     */
    private fun move(source: Vector2, target: Vector2, vel: Vector2, speed: Float, dt: Float) {
        if (source.x < target.x) {
            if (source.x + speed * dt > target.x) {
                vel.x = 0f
                source.x = target.x
            } else
                vel.x = speed
        }
        if (source.x > target.x) {
            if (source.x - speed * dt < target.x) {
                vel.x = 0f
                source.x = target.x
            } else
                vel.x = -speed
        }
        if (source.y < target.y) {
            if (source.y + speed * dt > target.y) {
                vel.y = 0f
                source.y = target.y
            } else
                vel.y = speed
        }
        if (source.y > target.y) {
            if (source.y - speed * dt < target.y) {
                vel.y = 0f
                source.y = target.y
            } else
                vel.y = -speed
        }
    }

}
