package com.questmark.entity.systems.enemy

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.questmark.entity.Mapper
import com.questmark.entity.components.enemy.EnemyComponent
import com.questmark.entity.components.enemy.HorizontalMovementComponent

import java.util.HashMap

/**
 * A LibGDX Ashley [EntitySystem] that handles the movement of enemies. In this system,
 * enemies move back and forth in a horizontal path determined by frequency of direction change.
 *
 * @author Ming Li
 */
class HorizontalMovementSystem : IteratingSystem(Family.all(EnemyComponent::class.java, HorizontalMovementComponent::class.java).get()) {

    private lateinit var targets: MutableMap<Entity, Float>

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        targets = HashMap()
        for (e in this.entities) {
            val pos = Mapper.POS_MAPPER.get(e)
            val hor = Mapper.HOR_MOVE_MAPPER.get(e)
            val vel = Mapper.VEL_MAPPER.get(e)
            val mag = Mapper.SPEED_MAPPER.get(e)
            val p = MathUtils.randomBoolean()
            if (p) {
                targets[e] = pos.p.x + hor.dist
                vel.v.x = mag.speed
            } else {
                targets[e] = pos.p.x - hor.dist
                vel.v.x = -mag.speed
            }
        }
    }

    override fun processEntity(entity: Entity, dt: Float) {
        val agg = Mapper.AGGRESSION_MAPPER.get(entity)
        val pos = Mapper.POS_MAPPER.get(entity)
        val hor = Mapper.HOR_MOVE_MAPPER.get(entity)

        if (agg != null) {
            if (agg.atSource) {
                if (pos.p.x == targets[entity])
                    targets[entity] = if (MathUtils.randomBoolean()) pos.p.x + hor.dist else pos.p.x - hor.dist
                handleMovement(entity)
            }
        } else
            handleMovement(entity)
    }

    private fun handleMovement(entity: Entity) {
        val pos = Mapper.POS_MAPPER.get(entity)
        val vel = Mapper.VEL_MAPPER.get(entity)
        val mag = Mapper.SPEED_MAPPER.get(entity)
        val hor = Mapper.HOR_MOVE_MAPPER.get(entity)

        val target = targets[entity]!!

        if (vel.v.x == 0f) {
            if (pos.p.x < target)
                vel.v.x = mag.speed
            else if (pos.p.x > target) vel.v.x = -mag.speed
        }

        if (vel.v.x > 0 && pos.p.x >= target) {
            targets[entity] = target - hor.dist
            vel.v.x = -mag.speed
        }
        if (vel.v.x < 0 && pos.p.x <= target) {
            targets[entity] = target + hor.dist
            vel.v.x = mag.speed
        }
    }

}
