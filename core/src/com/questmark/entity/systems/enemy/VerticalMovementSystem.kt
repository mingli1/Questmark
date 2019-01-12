package com.questmark.entity.systems.enemy

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.questmark.entity.Mapper
import com.questmark.entity.components.PositionComponent
import com.questmark.entity.components.SpeedComponent
import com.questmark.entity.components.VelocityComponent
import com.questmark.entity.components.enemy.AggressionComponent
import com.questmark.entity.components.enemy.EnemyComponent
import com.questmark.entity.components.enemy.VerticalMovementComponent

import java.util.HashMap

/**
 * A LibGDX Ashley [EntitySystem] that handles the movement of enemies. In this system,
 * enemies move back and forth in a vertical path determined by frequency of direction change.
 *
 * @author Ming Li
 */
class VerticalMovementSystem : IteratingSystem(Family.all(EnemyComponent::class.java, VerticalMovementComponent::class.java).get()) {

    private var targets: MutableMap<Entity, Float>? = null

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        targets = HashMap()
        for (e in this.entities) {
            val pos = Mapper.POS_MAPPER!!.get(e)
            val ver = Mapper.VER_MOVE_MAPPER!!.get(e)
            val vel = Mapper.VEL_MAPPER!!.get(e)
            val mag = Mapper.SPEED_MAPPER!!.get(e)
            val p = MathUtils.randomBoolean()
            if (p) {
                targets!![e] = pos.p.y + ver.dist
                vel.v.y = mag.speed
            } else {
                targets!![e] = pos.p.y - ver.dist
                vel.v.y = -mag.speed
            }
        }
    }

    override fun processEntity(entity: Entity, dt: Float) {
        val agg = Mapper.AGGRESSION_MAPPER!!.get(entity)
        val pos = Mapper.POS_MAPPER!!.get(entity)
        val ver = Mapper.VER_MOVE_MAPPER!!.get(entity)

        if (agg != null) {
            if (agg.atSource) {
                if (pos.p.y == targets!![entity])
                    targets!![entity] = if (MathUtils.randomBoolean()) pos.p.y + ver.dist else pos.p.y - ver.dist
                handleMovement(entity)
            }
        } else
            handleMovement(entity)
    }

    private fun handleMovement(entity: Entity) {
        val pos = Mapper.POS_MAPPER!!.get(entity)
        val vel = Mapper.VEL_MAPPER!!.get(entity)
        val mag = Mapper.SPEED_MAPPER!!.get(entity)
        val ver = Mapper.VER_MOVE_MAPPER!!.get(entity)

        val target = targets!![entity]!!

        if (vel.v.y == 0f) {
            if (pos.p.y < target)
                vel.v.y = mag.speed
            else if (pos.p.y > target) vel.v.y = -mag.speed
        }

        if (vel.v.y > 0 && pos.p.y >= target) {
            targets!![entity] = target - ver.dist
            vel.v.y = -mag.speed
        }
        if (vel.v.y < 0 && pos.p.y <= target) {
            targets!![entity] = target + ver.dist
            vel.v.y = mag.speed
        }
    }

}
