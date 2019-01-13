package com.questmark.entity.systems.enemy

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.questmark.entity.Mapper
import com.questmark.entity.components.enemy.CircularMovementComponent
import com.questmark.entity.components.enemy.EnemyComponent
import com.questmark.input.Direction

import java.util.HashMap

/**
 * A LibGDX Ashley [EntitySystem] that handles the movement of enemies. In this system,
 * enemies move in a circle by changing directions every given time interval.
 *
 * @author Ming Li
 */
class CircularMovementSystem : EntitySystem() {

    private lateinit var targets: MutableMap<Entity, Vector2>

    private lateinit var entities: ImmutableArray<Entity>
    // map of entity to its rotation scheme (true for clockwise, false for counterclockwise)
    private lateinit var rotationDir: MutableMap<Entity, Boolean>
    private lateinit var lastDir: MutableMap<Entity, Direction>

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(EnemyComponent::class.java, CircularMovementComponent::class.java).get())
        targets = HashMap()
        rotationDir = HashMap()
        lastDir = HashMap()

        for (e in entities) {
            val pos = Mapper.POS_MAPPER.get(e)
            targets[e] = Vector2(pos.p.x, pos.p.y)
            rotationDir[e] = MathUtils.randomBoolean()
            lastDir[e] = Direction.Up
        }
    }

    override fun update(dt: Float) {
        for (e in entities) {
            val agg = Mapper.AGGRESSION_MAPPER.get(e)

            if (agg != null) {
                if (agg.atSource) handleMovement(e)
            } else
                handleMovement(e)
        }
    }

    private fun handleMovement(e: Entity) {
        val pos = Mapper.POS_MAPPER.get(e)
        val vel = Mapper.VEL_MAPPER.get(e)
        val mag = Mapper.SPEED_MAPPER.get(e)
        val cir = Mapper.CIR_MOVE_MAPPER.get(e)

        val prevDir = lastDir[e]
        val target = targets[e]!!

        // entity is stopped during movement
        if (vel.v.x == 0f) {
            if (pos.p.x < target.x)
                vel.v.x = mag.speed
            else if (pos.p.x > target.x) vel.v.x = -mag.speed
        }
        if (vel.v.y == 0f) {
            if (pos.p.y < target.y)
                vel.v.y = mag.speed
            else if (pos.p.y > target.y) vel.v.y = -mag.speed
        }

        if (prevDir === Direction.Up && pos.p.y >= target.y ||
                prevDir === Direction.Right && pos.p.x >= target.x ||
                prevDir === Direction.Down && pos.p.y <= target.y ||
                prevDir === Direction.Left && pos.p.x <= target.x) {
            pos.p.x = target.x
            pos.p.y = target.y
            vel.v.y = 0f
            vel.v.x = vel.v.y

            if (rotationDir[e]!!) {
                when (lastDir[e]) {
                    Direction.Up -> {
                        vel.v.x = mag.speed
                        lastDir[e] = Direction.Right
                        targets[e] = target.set(target.x + cir.dist, target.y)
                    }
                    Direction.Right -> {
                        vel.v.y = -mag.speed
                        lastDir[e] = Direction.Down
                        targets[e] = target.set(target.x, target.y - cir.dist)
                    }
                    Direction.Down -> {
                        vel.v.x = -mag.speed
                        lastDir[e] = Direction.Left
                        targets[e] = target.set(target.x - cir.dist, target.y)
                    }
                    Direction.Left -> {
                        vel.v.y = mag.speed
                        lastDir[e] = Direction.Up
                        targets[e] = target.set(target.x, target.y + cir.dist)
                    }
                }
            } else {
                when (lastDir[e]) {
                    Direction.Up -> {
                        vel.v.x = -mag.speed
                        lastDir[e] = Direction.Left
                        targets[e] = target.set(target.x - cir.dist, target.y)
                    }
                    Direction.Left -> {
                        vel.v.y = -mag.speed
                        lastDir[e] = Direction.Down
                        targets[e] = target.set(target.x, target.y - cir.dist)
                    }
                    Direction.Down -> {
                        vel.v.x = mag.speed
                        lastDir[e] = Direction.Right
                        targets[e] = target.set(target.x + cir.dist, target.y)
                    }
                    Direction.Right -> {
                        vel.v.y = mag.speed
                        lastDir[e] = Direction.Up
                        targets[e] = target.set(target.x, target.y + cir.dist)
                    }
                }
            }
        }
    }

}
